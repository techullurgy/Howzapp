package com.techullurgy.conventions.plugins.core.kmp

import com.techullurgy.conventions.core.Libs
import com.techullurgy.conventions.extensions.core.KmpConfig
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

context(project: Project)
internal fun KotlinMultiplatformExtension.kmpConfigure(config: KmpConfig) {
    if(config.serialization) {
        project.pluginManager.apply(Libs.Plugins.kotlinSerializationPlugin)
    }
    sourceSets.commonMain.dependencies {
        if(config.serialization) {
            implementation(Libs.Dependencies.kotlinxSerializationJson)
        }
        if(config.datetime) {
            implementation(Libs.Dependencies.kotlinxDatetime)
        }
    }
}
