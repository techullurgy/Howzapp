package com.techullurgy.conventions.plugins.core.kmp

import com.techullurgy.conventions.extensions.core.WasmJsConfig
import com.techullurgy.conventions.extensions.core.WebType
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

@OptIn(ExperimentalWasmDsl::class)
internal fun KotlinMultiplatformExtension.wasmJsConfigure(config: WasmJsConfig) {
    if(config.enabled) {
        wasmJs {
            if(config.type == WebType.Browser) {
                browser()
            }
            if(config.type == WebType.NodeJs) {
                nodejs()
            }
        }
    }
}