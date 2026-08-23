package com.techullurgy.conventions.plugins.core.kmp

import com.techullurgy.conventions.core.Libs
import com.techullurgy.conventions.extensions.core.KoinConfig
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.koin.compiler.plugin.KoinGradleExtension

context(project: Project, kmpExtension: KotlinMultiplatformExtension)
internal fun koinConfigure(config: KoinConfig) {
    if (config.enabled) {
        with(project) {
            pluginManager.apply(Libs.Plugins.koinCompilerPlugin)

            with(kmpExtension) {
                sourceSets.apply {
                    commonMain.dependencies {
                        implementation(dependencies.platform(Libs.Dependencies.koinBom))
                        implementation(Libs.Dependencies.koinCore)
                        implementation(Libs.Dependencies.koinAnnotations)

                        if(config.compose) {
                            implementation(Libs.Dependencies.koinCompose)
                        }
                        if(config.composeViewmodel) {
                            implementation(Libs.Dependencies.koinComposeViewModel)
                        }
                        if(config.navigation3) {
                            implementation(Libs.Dependencies.koinComposeNavigation3)
                        }
                    }
                }
            }

            extensions.configure<KoinGradleExtension> {
                compileSafety.set(config.compileSafety)
            }
        }
    }
}