package com.techullurgy.conventions.extensions

import com.techullurgy.conventions.extensions.core.*
import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import javax.inject.Inject

open class KmpConventionPluginExtension @Inject constructor(
    objects: ObjectFactory
){
    internal val kmpConfig = objects.newInstance(KmpConfig::class.java)
    internal val androidConfig = objects.newInstance(AndroidConfig::class.java)
    internal val iosConfig = objects.newInstance(IosConfig::class.java)
    internal val jvmConfig = objects.newInstance(JvmConfig::class.java)
    internal val jsConfig = objects.newInstance(JsConfig::class.java)
    internal val wasmJsConfig = objects.newInstance(WasmJsConfig::class.java)
    internal val composeConfig = objects.newInstance(ComposeConfig::class.java)
    internal val koinConfig = objects.newInstance(KoinConfig::class.java)
    internal val room2Config = objects.newInstance(Room2Config::class.java)
    internal val room3Config = objects.newInstance(Room3Config::class.java)
    internal val navigation3Config = objects.newInstance(Navigation3Config::class.java)
    internal val ktorConfig = objects.newInstance(KtorConfig::class.java)

    internal var onKmpConfigure: ((KmpConfig) -> Unit)? = null
    internal var onAndroidConfigure: ((AndroidConfig) -> Unit)? = null
    internal var onIosConfigure: ((IosConfig) -> Unit)? = null
    internal var onJvmConfigure: ((JvmConfig) -> Unit)? = null
    internal var onJsConfigure: ((JsConfig) -> Unit)? = null
    internal var onWasmJsConfigure: ((WasmJsConfig) -> Unit)? = null
    internal var onComposeConfigure: ((ComposeConfig) -> Unit)? = null
    internal var onKoinConfigure: ((KoinConfig) -> Unit)? = null
    internal var onRoom2Configure: ((Room2Config) -> Unit)? = null
    internal var onRoom3Configure: ((Room3Config) -> Unit)? = null
    internal var onNavigation3Configure: ((Navigation3Config) -> Unit)? = null
    internal var onKtorConfigure: ((KtorConfig) -> Unit)? = null

    fun kmp(action: Action<KmpConfig>) {
        action.execute(kmpConfig)
        onKmpConfigure?.invoke(kmpConfig)
    }

    fun android(action: Action<AndroidConfig>) {
        action.execute(androidConfig)
        onAndroidConfigure?.invoke(androidConfig)
    }

    fun ios(action: Action<IosConfig>) {
        action.execute(iosConfig)
        onIosConfigure?.invoke(iosConfig)
    }

    fun jvm(action: Action<JvmConfig>) {
        action.execute(jvmConfig)
        onJvmConfigure?.invoke(jvmConfig)
    }

    fun js(action: Action<JsConfig>) {
        action.execute(jsConfig)
        onJsConfigure?.invoke(jsConfig)
    }

    fun wasm(action: Action<WasmJsConfig>) {
        action.execute(wasmJsConfig)
        onWasmJsConfigure?.invoke(wasmJsConfig)
    }

    fun compose(action: Action<ComposeConfig>) {
        action.execute(composeConfig)
        onComposeConfigure?.invoke(composeConfig)
    }

    fun koin(action: Action<KoinConfig>) {
        action.execute(koinConfig)
        onKoinConfigure?.invoke(koinConfig)
    }

    fun room2(action: Action<Room2Config>) {
        action.execute(room2Config)
        onRoom2Configure?.invoke(room2Config)
    }

    fun room3(action: Action<Room3Config>) {
        action.execute(room3Config)
        onRoom3Configure?.invoke(room3Config)
    }

    fun navigation3(action: Action<Navigation3Config>) {
        action.execute(navigation3Config)
        onNavigation3Configure?.invoke(navigation3Config)
    }

    fun ktor(action: Action<KtorConfig>) {
        action.execute(ktorConfig)
        onKtorConfigure?.invoke(ktorConfig)
    }
}