package com.techullurgy.howzapp.root.navigation.scenestrategies

import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.contains
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import com.techullurgy.howzapp.core.navigation.AppNavKey
import com.techullurgy.howzapp.core.navigation.TopLevelDestinationKey
import com.techullurgy.howzapp.root.navigation.scenes.HowzappHorizontalPagerScene

internal data class HowzappHorizontalPagerSceneStrategy<T: AppNavKey>(
    private val pages: List<T>
): SceneStrategy<T> {
    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        return if(shouldPermit(entries)) {
            HowzappHorizontalPagerScene(
                pages = pages,
                entries = entries,
                previousEntries = emptyList()
            )
        } else null
    }

    private fun shouldPermit(entries: List<NavEntry<T>>): Boolean {
        return entries.size == pages.size && entries.all { it.metadata.contains(TopLevelDestinationKey) }
    }
}