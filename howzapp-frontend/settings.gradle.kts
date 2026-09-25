rootProject.name = "howzapp-frontend"

val sharedVersions = java.util.Properties().apply {
    file("../gradle/versions-shared.properties").inputStream().use { load(it) }
}

pluginManagement {
    includeBuild("build-logic")

    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    includeBuild("../howzapp-common") {
        dependencySubstitution {
            substitute(module("com.techullurgy.howzapp:howzapp-common"))
                .using(project(":"))
        }
    }

    versionCatalogs {
        create("app") {
            from(files("../gradle/app.versions.toml"))
            version("kotlin", sharedVersions.getProperty("kotlin"))
            version("serialization", sharedVersions.getProperty("serialization"))
            version("coroutines", sharedVersions.getProperty("coroutines"))
            version("datetime", sharedVersions.getProperty("datetime"))
        }
        create("projectLibs") {
            from(files("../gradle/project.versions.toml"))
        }
    }

    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

// It enables the way of writing =>
// implementation(projects.core.preview.commonLibs)
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

//include(":app")

//include(":shared")

include(":core:navigation")
include(":core:qualifiers")
include(":core:domain")
include(":core:utils")
include(":core:network:websockets")
include(":core:network:http")
include(":core:network:fileupload")
include(":core:network:system")
include(":core:files")
include(":core:session")
include(":core:database")

//include(":core:ui:filepicker")
//include(":core:ui:permissions")

include(":base:network")
include(":base:session")

include(":root:database")
include(":root:navigation")

include(":infra:sync:api")
include(":infra:sync:impl")

include(":feature:common:db")
include(":feature:common:data")
include(":feature:common:domain:api")
include(":feature:common:domain:impl")

include(":feature:users:domain:api")

//include(":feature:chats:di")
include(":feature:chats:db")
include(":feature:chats:data")
include(":feature:chats:domain:api")
include(":feature:chats:domain:impl")
include(":feature:chats:presentation:api")
//include(":feature:chats:presentation:impl")