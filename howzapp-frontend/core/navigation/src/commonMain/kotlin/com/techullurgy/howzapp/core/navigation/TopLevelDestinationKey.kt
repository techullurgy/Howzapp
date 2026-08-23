package com.techullurgy.howzapp.core.navigation

import androidx.navigation3.runtime.NavMetadataKey
import androidx.navigation3.runtime.metadata

data object TopLevelDestinationKey: NavMetadataKey<Unit>

class TopLevelDestination private constructor() {
    companion object {
        fun applyAsDestination() = metadata {
            put(TopLevelDestinationKey, Unit)
        }
    }
}