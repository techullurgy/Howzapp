package com.techullurgy.conventions.plugins.core.kmp

import com.techullurgy.conventions.extensions.core.IosConfig
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal fun KotlinMultiplatformExtension.iosConfigure(config: IosConfig) {
    if (config.enabled.get()) {
        val iosTargets = listOf(
            iosArm64(),
            iosSimulatorArm64()
        )

        config.frameworkName.orNull?.let {
            iosTargets.forEach { target ->
                target.binaries.framework {
                    baseName = config.frameworkName.get()
                    isStatic = config.isStatic.get()
                }
            }
        }
    }
}