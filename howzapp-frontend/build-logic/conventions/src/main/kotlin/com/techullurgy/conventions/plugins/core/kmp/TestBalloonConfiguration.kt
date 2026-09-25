package com.techullurgy.conventions.plugins.core.kmp

import com.techullurgy.conventions.core.Libs
import com.techullurgy.conventions.extensions.core.TestBalloonConfig
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

context(project: Project, kmpExtension: KotlinMultiplatformExtension)
internal fun testBalloonConfigure(config: TestBalloonConfig) {
    if(config.enabled) {
        with(project) {
            pluginManager.apply(Libs.Plugins.testBalloonPlugin)
        }

        with(kmpExtension) {
            sourceSets.apply {
                commonTest.dependencies {
                    implementation(Libs.Dependencies.testBalloonFrameworkCore)
                }

                if(config.robolectric) {
                    findByName("androidHostTest")?.dependencies {
                        implementation(Libs.Dependencies.testBalloonIntegrationRobolectric)
                    }
                }
            }
        }
    }
}