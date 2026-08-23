package com.techullurgy.howzapp.root.navigation.scenedecorators

import androidx.navigation3.runtime.contains
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneDecoratorStrategy
import androidx.navigation3.scene.SceneDecoratorStrategyScope
import com.techullurgy.howzapp.core.navigation.AppNavKey
import com.techullurgy.howzapp.root.navigation.scenes.BottomTab
import com.techullurgy.howzapp.root.navigation.scenes.HowzappBottomNavigationScene

internal class BottomNavigationSceneDecoratorStrategy<T: AppNavKey>(
    private val tabs: List<BottomTab>
): SceneDecoratorStrategy<T> {
    override fun SceneDecoratorStrategyScope<T>.decorateScene(scene: Scene<T>): Scene<T> {
        return if(shouldDecorate(scene)) {
            HowzappBottomNavigationScene(
                tabs = tabs,
                scene = scene
            )
        } else scene
    }

    private fun shouldDecorate(scene: Scene<T>): Boolean {
        return scene.metadata.contains(BottomNavigationDecoratorKey)
    }
}