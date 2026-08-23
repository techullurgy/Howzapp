package com.techullurgy.howzapp.root.navigation.core

import com.techullurgy.howzapp.core.navigation.AppNavKey
import kotlinx.serialization.modules.PolymorphicModuleBuilder

typealias PolymorphicModuleInstaller = PolymorphicModuleBuilder<AppNavKey>.() -> Unit