# python3 -m pip install --upgrade certifi jproperties
# python3 file.py /path/to/toml --properties /path/to/properties
# Note: Paths can be Absolute / Relative
# python3 file.py --help

import ssl
import certifi
import urllib.request
import urllib.error
import xml.etree.ElementTree as ET
import tomllib
import argparse
from pathlib import Path
from jproperties import Properties

# CATALOG = Path("gradle/libs.versions.toml")

SSL_CONTEXT = ssl.create_default_context(cafile=certifi.where())

# Repositories to search in priority order
REPOSITORIES = [
    ("Maven Central", "https://repo1.maven.org/maven2"),
    ("Google Maven", "https://dl.google.com/dl/android/maven2"),
    ("Gradle Portal", "https://plugins.gradle.org/m2"),
]

def load_properties(file_path: Path) -> dict:
    """Parses a Java/Gradle .properties file into a Python dict."""
    if not file_path.exists():
        return None

    p = Properties()
    with file_path.open("rb") as f:
        p.load(f, "utf-8")

    # Access as a dictionary
    versions = {k: v.data for k, v in p.items()}
    return versions


def resolve_version(value, versions, properties=None):
    """Resolves version from direct string, TOML versions, or fallback properties."""
    if properties is None:
        properties = {}

    if isinstance(value, str):
        return value

    if isinstance(value, dict):
        if "ref" in value:
            ref_key = value["ref"]
            # 1. Check in [versions] of TOML
            if ref_key in versions:
                return versions[ref_key]
            # 2. Fallback to properties file
            if ref_key in properties:
                return properties[ref_key]
            return None

        if "required" in value:
            return value["required"]
        if "strictly" in value:
            return value["strictly"]
        if "prefer" in value:
            return value["prefer"]

    return None


def fetch_metadata_version(base_url, group, artifact):
    """Fetches maven-metadata.xml from a specific repo and extracts the latest release."""
    group_path = group.replace(".", "/")
    url = f"{base_url}/{group_path}/{artifact}/maven-metadata.xml"

    req = urllib.request.Request(
        url,
        headers={"User-Agent": "Gradle-Catalog-Checker/1.0"}
    )

    try:
        with urllib.request.urlopen(req, timeout=8, context=SSL_CONTEXT) as response:
            xml_data = response.read()

        root = ET.fromstring(xml_data)
        versioning = root.find("versioning")
        if versioning is not None:
            # 1. Prefer explicit <release>
            release = versioning.find("release")
            if release is not None and release.text:
                return release.text.strip()

            # 2. Check <latest>
            latest = versioning.find("latest")
            if latest is not None and latest.text:
                return latest.text.strip()

            # 3. Fall back to the newest version in the list
            versions = versioning.find("versions")
            if versions is not None:
                v_list = [v.text.strip() for v in versions.findall("version") if v.text]
                if v_list:
                    return v_list[-1]

    except urllib.error.HTTPError:
        pass
    except Exception:
        pass

    return None


def find_latest_version(group, artifact):
    """Iterates across Maven Central, Google Maven, and Gradle Portal."""
    for _, base_url in REPOSITORIES:
        version = fetch_metadata_version(base_url, group, artifact)
        if version:
            return version
    return None


def extract_group_and_artifact(declaration):
    """Extracts (group, artifact) from string, module, or {group, name} map."""
    if isinstance(declaration, str):
        if ":" in declaration:
            parts = declaration.split(":", 1)
            return parts[0], parts[1]
        return None, None

    if isinstance(declaration, dict):
        if "module" in declaration and ":" in declaration["module"]:
            parts = declaration["module"].split(":", 1)
            return parts[0], parts[1]
        
        group = declaration.get("group")
        name = declaration.get("name")
        if group and name:
            return group, name

    return None, None


def check_libraries(catalog, properties=None):
    versions = catalog.get("versions", {})
    libraries = catalog.get("libraries", {})
    updates = []

    for name, declaration in libraries.items():
        group, artifact = extract_group_and_artifact(declaration)
        current_version = resolve_version(
            declaration.get("version") if isinstance(declaration, dict) else None,
            versions,
            properties
        )

        if not group or not artifact or not current_version:
            continue

        latest = find_latest_version(group, artifact)

        if latest and latest != current_version:
            updates.append((name, f"{group}:{artifact}", current_version, latest))

    if updates:
        print("\n" + "=" * 105)
        print("LIBRARIES REQUIRING UPDATES")
        print("=" * 105)
        print(f"{'LIBRARY':<35} {'MODULE':<45} {'UPDATE'}")
        print("-" * 105)
        for name, module, current, latest in updates:
            print(f"{name:<35} {module:<45} {current} -> {latest}")


def check_plugins(catalog, properties=None):
    versions = catalog.get("versions", {})
    plugins = catalog.get("plugins", {})
    updates = []

    for name, declaration in plugins.items():
        if isinstance(declaration, dict):
            plugin_id = declaration.get("id")
            current_version = resolve_version(declaration.get("version"), versions, properties)
        else:
            plugin_id = declaration
            current_version = None

        if not plugin_id or not current_version:
            continue

        group = plugin_id
        artifact = f"{plugin_id}.gradle.plugin"

        latest = find_latest_version(group, artifact)

        if latest and latest != current_version:
            updates.append((name, plugin_id, current_version, latest))

    if updates:
        print("\n" + "=" * 105)
        print("PLUGINS REQUIRING UPDATES")
        print("=" * 105)
        print(f"{'PLUGIN':<35} {'PLUGIN ID':<45} {'UPDATE'}")
        print("-" * 105)
        for name, plugin_id, current, latest in updates:
            print(f"{name:<35} {plugin_id:<45} {current} -> {latest}")


def main():
    parser = argparse.ArgumentParser(description="Check for Library and Plugin updates in a Gradle Version Catalog")
    parser.add_argument(
        "catalog_path",
        nargs="?",
        type=Path,
        default=Path("gradle/libs.versions.toml"),
        help="Path to libs.versions.toml (default: gradle/libs.versions.toml)",
    )
    parser.add_argument(
        "--properties",
        type=Path,
        default=Path("shared.versions.properties"),
        help="Path to shared properties file (default: shared.versions.properties)",
    )

    args = parser.parse_args()
    catalog_file: Path = args.catalog_path
    properties_file: Path = args.properties

    properties = load_properties(properties_file)
    if properties:
        print(f"Loaded {len(properties)} properties from {properties_file}")

#    if not CATALOG.exists():
#        raise FileNotFoundError(f"Version catalog not found: {CATALOG}")
#
#    with CATALOG.open("rb") as file:
#        catalog = tomllib.load(file)

    if not catalog_file.exists():
        raise FileNotFoundError(f"Version catalog not found: {catalog_file}")

    with catalog_file.open("rb") as file:
        catalog = tomllib.load(file)

    print(f"Checking catalog: {catalog_file}")
    check_libraries(catalog,properties)
    check_plugins(catalog,properties)
    print(f"Checking catalog COMPLETED: {catalog_file}")


if __name__ == "__main__":
    main()
