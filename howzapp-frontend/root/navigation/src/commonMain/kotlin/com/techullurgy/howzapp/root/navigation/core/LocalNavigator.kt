package com.techullurgy.howzapp.root.navigation.core

import androidx.compose.runtime.staticCompositionLocalOf
import com.techullurgy.howzapp.core.navigation.AppNavKey
import com.techullurgy.howzapp.core.navigation.Navigator

internal val LocalNavigator = staticCompositionLocalOf<Navigator<AppNavKey>> { error("No Navigator Provided") }