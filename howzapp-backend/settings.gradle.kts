rootProject.name = "howzapp-backend"

val sharedVersions = java.util.Properties().apply {
    file("../gradle/versions-shared.properties").inputStream().use { load(it) }
}

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("server") {
            from(files("../gradle/server.versions.toml"))
            version("kotlin", sharedVersions.getProperty("kotlin"))
            version("serialization", sharedVersions.getProperty("serialization"))
            version("coroutines", sharedVersions.getProperty("coroutines"))
            version("datetime", sharedVersions.getProperty("datetime"))
        }
    }

    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(":common")
include(":websocket-service")
include(":user-service")
include(":media-service")
include(":sync-service")
include(":conversation-service")
include(":status-service")