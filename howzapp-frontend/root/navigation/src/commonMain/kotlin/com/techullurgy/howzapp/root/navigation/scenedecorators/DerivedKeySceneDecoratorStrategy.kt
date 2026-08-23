package com.techullurgy.howzapp.root.navigation.scenedecorators

import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneDecoratorStrategy
import androidx.navigation3.scene.SceneDecoratorStrategyScope
import com.techullurgy.howzapp.core.navigation.AppNavKey
import com.techullurgy.howzapp.root.navigation.scenes.DerivedKeyScene

internal class DerivedKeySceneDecoratorStrategy<T: AppNavKey>: SceneDecoratorStrategy<T> {
    override fun SceneDecoratorStrategyScope<T>.decorateScene(scene: Scene<T>): Scene<T> {
        return DerivedKeyScene(scene = scene)
    }
}