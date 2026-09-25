rootProject.name = "build-logic"

val sharedVersions = java.util.Properties().apply {
    file("../../gradle/versions-shared.properties").inputStream().use { load(it) }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    versionCatalogs {
        create("appLibs") {
            from(files("../../gradle/app.versions.toml"))
            version("kotlin", sharedVersions.getProperty("kotlin"))
            version("serialization", sharedVersions.getProperty("serialization"))
            version("coroutines", sharedVersions.getProperty("coroutines"))
            version("datetime", sharedVersions.getProperty("datetime"))
        }
        create("projectLibs") {
            from(files("../../gradle/project.versions.toml"))
        }
    }
}

include(":conventions")