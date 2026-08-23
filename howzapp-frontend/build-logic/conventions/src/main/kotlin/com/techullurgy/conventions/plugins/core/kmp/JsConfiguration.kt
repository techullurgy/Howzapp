package com.techullurgy.conventions.plugins.core.kmp

import com.techullurgy.conventions.extensions.core.JsConfig
import com.techullurgy.conventions.extensions.core.WebType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal fun KotlinMultiplatformExtension.jsConfigure(config: JsConfig) {
    if(config.enabled) {
        js {
            if(config.type == WebType.Browser) {
                browser()
            }
            if(config.type == WebType.NodeJs) {
                nodejs()
            }
        }
    }
}
