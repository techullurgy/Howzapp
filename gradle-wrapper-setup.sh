#!/usr/bin/env sh

set -eu

: "${GRADLE_DISTRIBUTION_URL:?GRADLE_DISTRIBUTION_URL environment variable must be set}"

# This script is located at $ROOT/gradle
ROOT="$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)"

PROJECTS_PATH="
howzapp-backend
howzapp-frontend
"

TEMPLATE="$ROOT/gradle/wrapper/gradle-wrapper.properties.template"
WRAPPER_JAR="$ROOT/gradle/wrapper/gradle-wrapper.jar"

[ -f "$TEMPLATE" ] || {
    echo "Template file not found: $TEMPLATE" >&2
    exit 1
}

[ -f "$WRAPPER_JAR" ] || {
    echo "Gradle wrapper JAR not found: $WRAPPER_JAR" >&2
    exit 1
}

for PROJECT_PATH in $PROJECTS_PATH; do
    PROJECT_GRADLE="$ROOT/$PROJECT_PATH/gradle"
    PROJECT_WRAPPER="$PROJECT_GRADLE/wrapper"

    TARGET="$PROJECT_WRAPPER/gradle-wrapper.properties"
    TARGET_JAR="$PROJECT_WRAPPER/gradle-wrapper.jar"

    echo "Processing $PROJECT_PATH..."

    # 1. Create gradle/wrapper directory if it doesn't exist
    mkdir -p "$PROJECT_WRAPPER"

    # 2. Copy gradle-wrapper.jar only if it doesn't exist
    if [ ! -f "$TARGET_JAR" ]; then
        echo "  Copying gradle-wrapper.jar"
        cp "$WRAPPER_JAR" "$TARGET_JAR"
    else
        echo "  gradle-wrapper.jar already exists"
    fi

    # 3. Always generate gradle-wrapper.properties
    echo "  Generating gradle-wrapper.properties"

    sed "s|@GRADLE_DISTRIBUTION_URL@|$GRADLE_DISTRIBUTION_URL|g" \
        "$TEMPLATE" > "$TARGET"
done