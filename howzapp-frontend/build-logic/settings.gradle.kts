rootProject.name = "build-logic"

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    versionCatalogs {
        create("appLibs") {
            from(files("../../gradle/app.versions.toml"))
        }
        create("projectLibs") {
            from(files("../../gradle/project.versions.toml"))
        }
    }
}

include(":conventions")