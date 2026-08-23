package com.techullurgy.howzapp.root.navigation.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.setValue
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.runtime.serialization.NavBackStackSerializer
import androidx.savedstate.compose.serialization.serializers.MutableStateSerializer
import androidx.savedstate.serialization.SavedStateConfiguration
import com.techullurgy.howzapp.core.navigation.AppNavKey

internal class NavigationState(
    val startRoute: AppNavKey,
    topLevelRoute: MutableState<AppNavKey>,
    val backStacks: Map<AppNavKey, NavBackStack<AppNavKey>>
) {
    var topLevelRoute: AppNavKey by topLevelRoute
        private set
    private val topLevelBackStack = NavBackStack(*backStacks.keys.toTypedArray())

    val inTopLevelMode: Boolean
        get() = backStacks.values.map { it.size <= 1 }.all { it }

    val currentBackStack: NavBackStack<AppNavKey>
        get() {
            return if(inTopLevelMode) {
                topLevelBackStack
            } else {
                backStacks[topLevelRoute] ?: error("No Backstack found for NavKey: $topLevelRoute")
            }
        }

    fun changeTopLevelRoute(key: AppNavKey) {
        if(key == topLevelRoute) return
        if(!inTopLevelMode) {
            clearCurrentBackstack()
        }
        topLevelRoute = key
    }

    private fun clearCurrentBackstack() {
        require(currentBackStack !== topLevelBackStack)
        currentBackStack.dropLast(currentBackStack.size - 1)
    }
}

@Composable
internal fun rememberNavigationState(
    startRoute: AppNavKey,
    topLevelRoutes: List<AppNavKey>,
    savedStateConfiguration: SavedStateConfiguration
): NavigationState {
    require(topLevelRoutes.contains(startRoute))

    val topLevelRoute = rememberSerializable(
        startRoute, topLevelRoutes,
        configuration = savedStateConfiguration,
        serializer = MutableStateSerializer<AppNavKey>()
    ) {
        mutableStateOf(startRoute)
    }

    val backStacks = topLevelRoutes.associateWith { key ->
        rememberSerializable(
            configuration = savedStateConfiguration,
            serializer = NavBackStackSerializer<AppNavKey>()
        ) {
            NavBackStack(key)
        }
    }

    return remember(startRoute, topLevelRoutes) {
        NavigationState(startRoute, topLevelRoute, backStacks)
    }
}

@Composable
internal fun NavigationState.toEntries(
    entryProvider: (AppNavKey) -> NavEntry<AppNavKey>
): List<NavEntry<AppNavKey>> {
    return rememberDecoratedNavEntries(
        backStack = currentBackStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator()
        ),
        entryProvider = entryProvider
    )
}