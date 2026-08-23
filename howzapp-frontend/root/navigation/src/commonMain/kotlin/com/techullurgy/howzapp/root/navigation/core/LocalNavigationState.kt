package com.techullurgy.howzapp.root.navigation.core

import androidx.compose.runtime.staticCompositionLocalOf

internal val LocalNavigationState = staticCompositionLocalOf<NavigationState> { error("No NavigationState Provided") }