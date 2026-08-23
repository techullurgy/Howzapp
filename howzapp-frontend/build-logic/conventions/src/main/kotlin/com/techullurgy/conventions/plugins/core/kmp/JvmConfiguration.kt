package com.techullurgy.conventions.plugins.core.kmp

import com.techullurgy.conventions.extensions.core.JvmConfig
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal fun KotlinMultiplatformExtension.jvmConfigure(config: JvmConfig) {
    if(config.enabled) {
        jvm()
    }
}