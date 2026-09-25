package com.techullurgy.howzapp.infra.sync.api

sealed interface SyncConnectionStatus {
    data object Connected: SyncConnectionStatus
    data object Connecting: SyncConnectionStatus
    data object Disconnected: SyncConnectionStatus
}