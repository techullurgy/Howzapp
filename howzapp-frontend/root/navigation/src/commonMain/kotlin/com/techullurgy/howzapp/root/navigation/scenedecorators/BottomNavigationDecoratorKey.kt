package com.techullurgy.howzapp.root.navigation.scenedecorators

import androidx.navigation3.runtime.NavMetadataKey
import androidx.navigation3.runtime.metadata

data object BottomNavigationDecoratorKey: NavMetadataKey<Unit>

class BottomNavigationDecorator private constructor() {
    companion object {
        fun decorate() = metadata {
            put(BottomNavigationDecoratorKey, Unit)
        }
    }
}