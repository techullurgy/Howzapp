package com.techullurgy.conventions.plugins.core.kmp

import com.techullurgy.conventions.core.Libs
import com.techullurgy.conventions.extensions.core.Navigation3Config
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

context(project: Project, kmpExtension: KotlinMultiplatformExtension)
internal fun nav3Configure(config: Navigation3Config) {
    if (config.enabled) {
        with(project) {
            if(config.serialization) {
                pluginManager.apply(Libs.Plugins.kotlinSerializationPlugin)
            }

            with(kmpExtension) {
                sourceSets.apply {
                    commonMain.dependencies {
                        if(config.runtime) {
                            implementation(Libs.Dependencies.navigation3Runtime)
                        }
                        if(config.ui) {
                            implementation(Libs.Dependencies.jetbrainsNavigation3Ui)
                        }
                    }
                }
            }
        }
    }
}