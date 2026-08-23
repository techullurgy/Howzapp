package com.techullurgy.conventions.core

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension

object Libs {
    context(project: Project)
    private val appLibs: VersionCatalog get() = project
        .extensions
        .getByType(VersionCatalogsExtension::class.java)
        .named("app")

    context(project: Project)
    private val projectLibs: VersionCatalog get() = project
        .extensions
        .getByType(VersionCatalogsExtension::class.java)
        .named("project")

    context(_: Project) val rootNamespace: String get() = projectLibs.findVersion("projectApplicationId").get().toString()

    object Versions {
        context(_: Project) val javaVersion: String get() = projectLibs.findVersion("javaVersion").get().toString()
        context(_: Project) val compileSdk: Int get() = projectLibs.findVersion("android-compileSdk").get().toString().toInt()
        context(_: Project) val minSdk: Int get() = projectLibs.findVersion("android-minSdk").get().toString().toInt()
        context(_: Project) val targetSdk: Int get() = projectLibs.findVersion("android-targetSdk").get().toString().toInt()
    }

    object Plugins {
        context(_: Project) val kotlinMultiplatformPlugin: String get() = appLibs.findPlugin("kotlinMultiplatform").get().get().pluginId
        context(_: Project) val composeMultiplatformPlugin: String get() = appLibs.findPlugin("composeMultiplatform").get().get().pluginId
        context(_: Project) val composeCompilerPlugin: String get() = appLibs.findPlugin("composeCompiler").get().get().pluginId
        context(_: Project) val androidKmpLibraryPlugin: String get() = appLibs.findPlugin("androidKmpLibrary").get().get().pluginId
        context(_: Project) val androidApplicationPlugin: String get() = appLibs.findPlugin("androidApplication").get().get().pluginId
        context(_: Project) val androidLibraryPlugin: String get() = appLibs.findPlugin("androidLibrary").get().get().pluginId

        context(_: Project) val kotlinJvmPlugin: String get() = appLibs.findPlugin("kotlin-jvm").get().get().pluginId
        context(_: Project) val kotlinSerializationPlugin: String get() = appLibs.findPlugin("kotlin-serialization").get().get().pluginId

        context(_: Project) val koinCompilerPlugin: String get() = appLibs.findPlugin("koinCompiler").get().get().pluginId
        context(_: Project) val kspPlugin: String get() = appLibs.findPlugin("ksp").get().get().pluginId
        context(_: Project) val roomPlugin: String get() = appLibs.findPlugin("room").get().get().pluginId
        context(_: Project) val room3Plugin: String get() = appLibs.findPlugin("room3").get().get().pluginId
        context(_: Project) val roborazziPlugin: String get() = appLibs.findPlugin("roborazzi").get().get().pluginId
    }

    object Dependencies {
        context(_: Project) val composeRuntime get()= appLibs.findLibrary("compose-runtime").get()
        context(_: Project) val composeFoundation get()= appLibs.findLibrary("compose-foundation").get()
        context(_: Project) val composeUi get()= appLibs.findLibrary("compose-ui").get()
        context(_: Project) val composeMaterial3 get()= appLibs.findLibrary("compose-material3").get()
        context(_: Project) val composeComponentsResources get()= appLibs.findLibrary("compose-components-resources").get()
        context(_: Project) val composeUiTooling get()= appLibs.findLibrary("compose-ui-tooling").get()
        context(_: Project) val composeUiToolingPreview get()= appLibs.findLibrary("compose-ui-tooling-preview").get()
        context(_: Project) val androidxLifecycleViewmodelCompose get()= appLibs.findLibrary("androidx-lifecycle-viewmodelCompose").get()
        context(_: Project) val androidxLifecycleRuntimeCompose get()= appLibs.findLibrary("androidx-lifecycle-runtimeCompose").get()

        context(_: Project) val androidxComposeUitestJunit4Android get()= appLibs.findLibrary("androidx-compose-uitest-junit4-android").get()
        context(_: Project) val androidxComposeUitestManifest get()= appLibs.findLibrary("androidx-compose-uitest-manifest").get()

        context(_: Project) val kotlinxCoroutinesCore get() = appLibs.findLibrary("kotlinx-coroutines-core").get()
        context(_: Project) val kotlinxSerializationJson get() = appLibs.findLibrary("kotlinx-serialization-json").get()
        context(_: Project) val kotlinxDatetime get() = appLibs.findLibrary("kotlinx-datetime").get()
        context(_: Project) val kotlinxCoroutinesTest get() = appLibs.findLibrary("kotlinx-coroutines-test").get()
        context(_: Project) val kotlinTest get() = appLibs.findLibrary("kotlin-test").get()
        context(_: Project) val assertk get() = appLibs.findLibrary("assertk").get()

        context(_: Project) val koinBom get() = appLibs.findLibrary("koin-bom").get()
        context(_: Project) val koinCore get() = appLibs.findLibrary("koin-core").get()
        context(_: Project) val koinAnnotations get() = appLibs.findLibrary("koin-annotations").get()
        context(_: Project) val koinCompose get() = appLibs.findLibrary("koin-compose").get()
        context(_: Project) val koinComposeViewModel get() = appLibs.findLibrary("koin-compose-viewmodel").get()
        context(_: Project) val koinComposeNavigation3 get() = appLibs.findLibrary("koin-compose-navigation3").get()

        context(_: Project) val navigation3Runtime get() = appLibs.findLibrary("androidx-navigation3-runtime").get()
        context(_: Project) val jetbrainsNavigation3Ui get() = appLibs.findLibrary("jetbrains-androidx-navigation3-ui").get()

        context(_: Project) val roomCompiler get() = appLibs.findLibrary("androidx-room-compiler").get()
        context(_: Project) val roomRuntime get() = appLibs.findLibrary("androidx-room-runtime").get()
        context(_: Project) val roomTesting get() = appLibs.findLibrary("androidx-room-testing").get()
        context(_: Project) val room3Compiler get() = appLibs.findLibrary("androidx-room3-compiler").get()
        context(_: Project) val room3Runtime get() = appLibs.findLibrary("androidx-room3-runtime").get()
        context(_: Project) val room3Testing get() = appLibs.findLibrary("androidx-room3-testing").get()
        context(_: Project) val room3Paging get() = appLibs.findLibrary("androidx-room3-paging").get()
        context(_: Project) val sqliteBundled get() = appLibs.findLibrary("sqlite-bundled").get()

        context(_: Project) val robolectric get() = appLibs.findLibrary("robolectric").get()
        context(_: Project) val roborazzi get() = appLibs.findLibrary("roborazzi").get()
        context(_: Project) val roborazziCompose get() = appLibs.findLibrary("roborazzi-compose").get()
        context(_: Project) val roborazziRule get() = appLibs.findLibrary("roborazzi-rule").get()

        context(_: Project) val ktorCore get() = appLibs.findLibrary("ktor-client-core").get()
        context(_: Project) val ktorCIO get() = appLibs.findLibrary("ktor-client-cio").get()
        context(_: Project) val ktorDarwin get() = appLibs.findLibrary("ktor-client-darwin").get()
        context(_: Project) val ktorContentNegotiation get() = appLibs.findLibrary("ktor-client-content-negotiation").get()
        context(_: Project) val ktorSerializationJson get() = appLibs.findLibrary("ktor-serialization-kotlinx-json").get()
        context(_: Project) val ktorLogging get() = appLibs.findLibrary("ktor-client-logging").get()
        context(_: Project) val ktorAuth get() = appLibs.findLibrary("ktor-client-auth").get()
        context(_: Project) val ktorWebsockets get() = appLibs.findLibrary("ktor-client-websockets").get()
    }
}