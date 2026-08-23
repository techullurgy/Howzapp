package com.techullurgy.conventions.plugins.core.kmp

import com.techullurgy.conventions.core.Libs
import com.techullurgy.conventions.extensions.KmpConventionPluginExtension
import com.techullurgy.conventions.extensions.core.ComposeConfig
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

context(project: Project, kmpExtension: KotlinMultiplatformExtension)
internal fun composeConfigure(config: ComposeConfig) {
    if(config.enabled) {
        with(project) {
            pluginManager.apply(Libs.Plugins.composeMultiplatformPlugin)
            pluginManager.apply(Libs.Plugins.composeCompilerPlugin)

            val kmpConventionExt = extensions.getByType(KmpConventionPluginExtension::class.java)

            with(kmpExtension) {
                sourceSets.apply {
                    commonMain.dependencies {
                        implementation(Libs.Dependencies.composeRuntime)
                        if(config.foundation) {
                            implementation(Libs.Dependencies.composeFoundation)
                        }
                        if(config.ui) {
                            implementation(Libs.Dependencies.composeUi)
                        }
                        if(config.resources) {
                            implementation(Libs.Dependencies.composeComponentsResources)
                        }
                        if(config.preview) {
                            implementation(Libs.Dependencies.composeUiToolingPreview)
                        }
                        implementation(Libs.Dependencies.androidxLifecycleViewmodelCompose)
                        implementation(Libs.Dependencies.androidxLifecycleRuntimeCompose)
                        if(config.material3) {
                            implementation(Libs.Dependencies.composeMaterial3)
                        }
                    }
                }
            }

            if(kmpConventionExt.androidConfig.enabled.get()) {
                if(config.preview) {
                    dependencies {
                        "androidRuntimeClasspath"(Libs.Dependencies.composeUiTooling)
                    }
                }
            }
        }
    }
}
