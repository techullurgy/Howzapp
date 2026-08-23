package com.techullurgy.howzapp.root.navigation.scenes

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.Scene
import com.techullurgy.howzapp.core.navigation.AppNavKey
import com.techullurgy.howzapp.root.navigation.core.LocalNavigationState
import com.techullurgy.howzapp.root.navigation.core.LocalNavigator
import com.techullurgy.howzapp.root.navigation.scenedecorators.BottomNavigationDecorator
import kotlinx.coroutines.flow.collectLatest

internal data class HowzappHorizontalPagerScene<T: AppNavKey>(
    private val pages: List<T>,
    override val entries: List<NavEntry<T>>,
    override val previousEntries: List<NavEntry<T>>
): Scene<T> {
    override val key: Any = this::class.simpleName!!

    override val content: @Composable (() -> Unit) = {
        val pagerState = rememberPagerState { pages.size }
        val navigator = LocalNavigator.current

        val currentTopLevelRoute = LocalNavigationState.current.topLevelRoute
        val targetIndex = pages.indexOf(currentTopLevelRoute)

        LaunchedEffect(targetIndex) {
            if(pagerState.currentPage != targetIndex) {
                pagerState.animateScrollToPage(targetIndex)
            }
        }

        LaunchedEffect(Unit) {
            snapshotFlow { pagerState.settledPage }
                .collectLatest {
                    navigator.navigate(pages[it])
                }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) {
            entries[it].Content()
        }
    }

    override val metadata: Map<String, Any>
        get() = super.metadata + BottomNavigationDecorator.decorate()
}