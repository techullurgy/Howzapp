rootProject.name = "howzapp-frontend"

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
        }
        create("project") {
            from(files("../gradle/project.versions.toml"))
        }
        create("common") {
            from(files("../gradle/common.versions.toml"))
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

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

//include(":app")

//include(":shared")

include(":core:navigation")
include(":core:domain")
include(":core:network:websockets")
include(":core:network:http")
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