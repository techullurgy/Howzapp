rootProject.name = "howzapp-common"

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
        create("app") {
            from(files("../gradle/app.versions.toml"))
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