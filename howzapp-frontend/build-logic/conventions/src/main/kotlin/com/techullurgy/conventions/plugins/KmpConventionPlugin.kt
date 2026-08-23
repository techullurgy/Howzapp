package com.techullurgy.conventions.plugins

import com.techullurgy.conventions.core.Libs
import com.techullurgy.conventions.extensions.KmpConventionPluginExtension
import com.techullurgy.conventions.plugins.core.kmp.*
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

@Suppress("unused")
class KmpConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Register the custom extension
            val extension = extensions.create<KmpConventionPluginExtension>("kmpConvention", objects)

            pluginManager.apply(Libs.Plugins.kotlinMultiplatformPlugin)

            extensions.configure<KotlinMultiplatformExtension> {
                compilerOptions {
                    freeCompilerArgs.addAll("-Xexplicit-backing-fields")
                    optIn.addAll("kotlinx.coroutines.ExperimentalCoroutinesApi")
                }
                sourceSets.apply {
                    commonMain.dependencies {
                        implementation(Libs.Dependencies.kotlinxCoroutinesCore)
                    }
                    commonTest.dependencies {
                        implementation(Libs.Dependencies.kotlinTest)
                        implementation(Libs.Dependencies.assertk)
                        implementation(Libs.Dependencies.kotlinxCoroutinesTest)
                    }
                }
            }

            extension.onKmpConfigure = {
                extensions.configure<KotlinMultiplatformExtension> {
                    kmpConfigure(it)
                }
            }

            extension.onAndroidConfigure = {
                extensions.configure<KotlinMultiplatformExtension> {
                    androidConfigure(it)
                }
            }

            extension.onIosConfigure = {
                extensions.configure<KotlinMultiplatformExtension> {
                    iosConfigure(it)
                }
            }

            extension.onJvmConfigure = {
                extensions.configure<KotlinMultiplatformExtension> {
                    jvmConfigure(it)
                }
            }

            extension.onJsConfigure = {
                extensions.configure<KotlinMultiplatformExtension> {
                    jsConfigure(it)
                }
            }

            extension.onWasmJsConfigure = {
                extensions.configure<KotlinMultiplatformExtension> {
                    wasmJsConfigure(it)
                }
            }

            extension.onRoom3Configure = {
                extensions.configure<KotlinMultiplatformExtension> {
                    room3Configure(it)
                }
            }

            extension.onKoinConfigure = {
                extensions.configure<KotlinMultiplatformExtension> {
                    koinConfigure(it)
                }
            }

            extension.onNavigation3Configure = {
                extensions.configure<KotlinMultiplatformExtension> {
                    nav3Configure(it)
                }
            }

            extension.onComposeConfigure = {
                extensions.configure<KotlinMultiplatformExtension> {
                    composeConfigure(it)
                }
            }

            extension.onKtorConfigure = {
                extensions.configure<KotlinMultiplatformExtension> {
                    ktorConfigure(it)
                }
            }

            extension.onRoom2Configure = {
            }
        }
    }
}
