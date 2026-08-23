package com.techullurgy.howzapp.root.navigation.core

import com.techullurgy.howzapp.core.navigation.AppNavKey
import com.techullurgy.howzapp.core.navigation.Navigator

internal class AppNavigator(val state: NavigationState): Navigator<AppNavKey> {
    override fun navigate(destination: AppNavKey) {
        // destination is a topLevelRoute
        if(state.backStacks.containsKey(destination)) {
            state.changeTopLevelRoute(destination)
        } else {
            state.backStacks[state.topLevelRoute]!!.add(destination)
        }
    }

    override fun goBack() {
        if(state.inTopLevelMode) {
            if(state.topLevelRoute == state.startRoute) {
                // Exit the app
                state.currentBackStack.clear()
            } else {
                state.changeTopLevelRoute(state.startRoute)
            }
        } else {
            state.currentBackStack.removeLastOrNull()
        }
    }
}