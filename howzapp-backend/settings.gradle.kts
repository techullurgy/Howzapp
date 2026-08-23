rootProject.name = "howzapp-backend"

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
        }
        create("common") {
            from(files("../gradle/common.versions.toml"))
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