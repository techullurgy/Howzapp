package com.techullurgy.conventions.plugins.core.kmp

import androidx.room3.gradle.RoomExtension
import com.techullurgy.conventions.core.Libs
import com.techullurgy.conventions.extensions.KmpConventionPluginExtension
import com.techullurgy.conventions.extensions.core.Room3Config
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

context(project: Project, kmpExtension: KotlinMultiplatformExtension)
internal fun room3Configure(config: Room3Config) {
    if(config.enabled) {
        with(project) {
            val kmpConventionExt = extensions.getByType(KmpConventionPluginExtension::class.java)

            if(config.compiler) {
                pluginManager.apply(Libs.Plugins.room3Plugin)
                pluginManager.apply(Libs.Plugins.kspPlugin)

                val androidEnabled = kmpConventionExt.androidConfig.enabled.get()
                val iosEnabled = kmpConventionExt.iosConfig.enabled.get()
                val jvmEnabled = kmpConventionExt.jvmConfig.enabled
                val jsEnabled = kmpConventionExt.jsConfig.enabled
                val wasmJsEnabled = kmpConventionExt.wasmJsConfig.enabled

                dependencies {
                    if(androidEnabled) {
                        add("kspAndroid", Libs.Dependencies.room3Compiler)
                    }

                    if(iosEnabled) {
                        add("kspIosArm64", Libs.Dependencies.room3Compiler)
                        add("kspIosSimulatorArm64", Libs.Dependencies.room3Compiler)
                    }

                    if(jvmEnabled) {
                        add("kspJvm", Libs.Dependencies.room3Compiler)
                    }

                    if(jsEnabled) {
                        add("kspJs", Libs.Dependencies.room3Compiler)
                    }

                    if(wasmJsEnabled) {
                        add("kspWasmJs", Libs.Dependencies.room3Compiler)
                    }
                }

                extensions.configure<RoomExtension> {
                    config.schemaDir?.let {
                        schemaDirectory(it)
                    } ?: throw IllegalStateException("Room 3 Schema Directory cannot be null")
                }
            }

            // TODO: Add SQLITE_BUNDLED on IOS + JVM, No (WEB_TARGETS + ANDROID)
            with(kmpExtension) {
                sourceSets.apply {
                    commonMain.dependencies {
                        implementation(Libs.Dependencies.room3Runtime)
                        if(config.paging) {
                            implementation(Libs.Dependencies.room3Paging)
                        }
                    }

                    if(config.compiler) {
                        findByName("androidMain")?.dependencies {
                            implementation(Libs.Dependencies.sqliteBundled)
                        }
                        findByName("iosMain")?.dependencies {
                            implementation(Libs.Dependencies.sqliteBundled)
                        }
                        findByName("jvmMain")?.dependencies {
                            implementation(Libs.Dependencies.sqliteBundled)
                        }
                    }

                    if(config.test) {
                        commonTest.dependencies {
                            implementation(Libs.Dependencies.room3Testing)
                        }
                    }
                }
            }
        }
    }
}