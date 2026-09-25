rootProject.name = "howzapp-common"

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
        create("app") {
            from(files("../gradle/app.versions.toml"))
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