package com.techullurgy.howzapp.root.navigation.scenes

import androidx.navigation3.scene.Scene
import com.techullurgy.howzapp.core.navigation.AppNavKey

internal data class DerivedKeyScene<T: AppNavKey>(
    private val scene: Scene<T>
): Scene<T> by scene {
    override val key: Any = scene::class to scene.key
}