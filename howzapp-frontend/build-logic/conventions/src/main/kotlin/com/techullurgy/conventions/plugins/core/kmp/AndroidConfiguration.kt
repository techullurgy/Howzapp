package com.techullurgy.conventions.plugins.core.kmp

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import com.techullurgy.conventions.core.Libs
import com.techullurgy.conventions.extensions.core.AndroidConfig
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

context(project: Project)
internal fun KotlinMultiplatformExtension.androidConfigure(config: AndroidConfig) {
    if (config.enabled.get()) {
        project.pluginManager.apply(Libs.Plugins.androidKmpLibraryPlugin)
        if(config.hostTest.get() && config.hostTestConfig.roborazzi) {
            project.pluginManager.apply(Libs.Plugins.roborazziPlugin)
        }

        // 2. Invoke the dynamic 'android' extension function on Kotlin Multiplatform
        // This instantiates the target and creates the 'main' compilation properly.
        val androidExtension = (this as ExtensionAware).extensions.findByName("android")

        if (androidExtension is KotlinMultiplatformAndroidLibraryTarget) {
            androidExtension.applyConfig(config)
        } else {
            // If the target doesn't exist yet, configure it via ExtensionAware DSL action
            (this as ExtensionAware).extensions.configure<KotlinMultiplatformAndroidLibraryTarget>("android") {
                applyConfig(config)
            }
        }

        // Configure Source Sets dynamically
        sourceSets.apply {
            if (config.hostTest.get()) {
                findByName("androidHostTest")?.dependencies {
                    if (config.hostTestConfig.robolectric) {
                        implementation(Libs.Dependencies.robolectric)
                        implementation(Libs.Dependencies.androidxComposeUitestJunit4Android)
                        implementation(Libs.Dependencies.androidxComposeUitestManifest)
                    }
                    if (config.hostTestConfig.roborazzi) {
                        implementation(Libs.Dependencies.roborazzi)
                        implementation(Libs.Dependencies.roborazziCompose)
                        implementation(Libs.Dependencies.roborazziRule)
                    }
                }
            }

            if (config.deviceTest.get()) {
                findByName("androidDeviceTest")?.dependencies {
                    if (config.deviceTestConfig.composeUiTest) {
                        implementation(Libs.Dependencies.androidxComposeUitestJunit4Android)
                        implementation(Libs.Dependencies.androidxComposeUitestManifest)
                    }
                }
            }
        }
    }
}

context(project: Project)
private fun KotlinMultiplatformAndroidLibraryTarget.applyConfig(config: AndroidConfig) {
    namespace = Libs.rootNamespace + "." + config.localNamespace
    compileSdk = Libs.Versions.compileSdk
    minSdk = Libs.Versions.minSdk

    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(Libs.Versions.javaVersion))
    }

    androidResources {
        enable = true
    }

    if (config.hostTest.get()) {
        withHostTest {
            isIncludeAndroidResources = true
        }
    }

    if (config.deviceTest.get()) {
        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = config.deviceTestConfig.runner
        }
    }
}