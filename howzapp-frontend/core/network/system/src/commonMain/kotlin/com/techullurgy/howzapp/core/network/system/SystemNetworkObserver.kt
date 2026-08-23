package com.techullurgy.howzapp.core.network.system

import kotlinx.coroutines.flow.Flow

interface SystemNetworkObserver {
    val networkState: Flow<SystemNetworkState>
}