package com.techullurgy.howzapp.core.network.system

sealed interface SystemNetworkState {
    data object Connected: SystemNetworkState
    data object NotConnected: SystemNetworkState
}