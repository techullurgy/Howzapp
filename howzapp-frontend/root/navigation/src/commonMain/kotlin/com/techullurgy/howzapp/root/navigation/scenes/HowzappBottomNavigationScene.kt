package com.techullurgy.howzapp.root.navigation.scenes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation3.scene.Scene
import com.techullurgy.howzapp.core.navigation.AppNavKey
import com.techullurgy.howzapp.root.navigation.core.LocalNavigationState
import com.techullurgy.howzapp.root.navigation.core.LocalNavigator

internal class HowzappBottomNavigationScene<T: AppNavKey>(
    private val tabs: List<BottomTab>,
    scene: Scene<T>
): Scene<T> by scene {
    override val content: @Composable (() -> Unit) = {
        val navigator = LocalNavigator.current
        val currentTopLevelRoute = LocalNavigationState.current.topLevelRoute

        val selectedTabIndex = tabs.indexOfFirst { it.route == currentTopLevelRoute }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            scene.content()

            BottomTabsBar(
                tabs = tabs,
                selectedTabIndex = selectedTabIndex,
                onTabClick = { navigator.navigate(it.route) },
                modifier = Modifier.padding(
                    start = 24.dp,
                    end = 24.dp,
                    bottom = 64.dp
                )
            )
        }
    }
}

data class BottomTab(
    val label: String,
    val route: AppNavKey,
)

@Composable
private fun BottomTabsBar(
    tabs: List<BottomTab>,
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    onTabClick: (BottomTab) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tabs.forEachIndexed { index, tab ->
                Box(
                    modifier = Modifier.weight(1f)
                        .height(50.dp)
                        .clickable(
                            onClick = { onTabClick(tab) }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    BasicText(
                        text = tab.label,
                        style = TextStyle(color = if (index == selectedTabIndex) Color.Green else Color.White)
                    )
                }
            }
        }
    }
}