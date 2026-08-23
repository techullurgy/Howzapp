package com.techullurgy.conventions.plugins.core.kmp

import com.techullurgy.conventions.core.Libs
import com.techullurgy.conventions.extensions.core.KtorConfig
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

context(project: Project, kmpExtension: KotlinMultiplatformExtension)
internal fun ktorConfigure(config: KtorConfig) {
    if (config.enabled) {
        with(project) {
            if(config.serialization) {
                pluginManager.apply(Libs.Plugins.kotlinSerializationPlugin)
            }

            with(kmpExtension) {
                sourceSets.apply {
                    commonMain.dependencies {
                        implementation(Libs.Dependencies.ktorCore)
                        implementation(Libs.Dependencies.ktorContentNegotiation)
                        implementation(Libs.Dependencies.ktorSerializationJson)

                        if(config.websocket) {
                            implementation(Libs.Dependencies.ktorWebsockets)
                        }
                        if(config.logging) {
                            implementation(Libs.Dependencies.ktorLogging)
                        }
                        if(config.auth) {
                            implementation(Libs.Dependencies.ktorAuth)
                        }
                    }
                    findByName("androidMain")?.dependencies {
                        implementation(Libs.Dependencies.ktorCIO)
                    }
                    findByName("iosMain")?.dependencies {
                        implementation(Libs.Dependencies.ktorDarwin)
                    }
                    findByName("jvmMain")?.dependencies {
                        implementation(Libs.Dependencies.ktorCIO)
                    }
                    findByName("webMain")?.dependencies {
                        implementation(Libs.Dependencies.ktorCIO)
                    }
                }
            }
        }
    }
}