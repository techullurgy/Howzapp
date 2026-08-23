package com.techullurgy.howzapp.core.domain

interface AppConnectionState {
    data object Connected: AppConnectionState
    data object Disconnected: AppConnectionState
    data object Connecting: AppConnectionState
}