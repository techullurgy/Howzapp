package com.techullurgy.howzapp.core.network.websockets

sealed interface WebSocketConnectionStatus {
    data object Connecting: WebSocketConnectionStatus
    data object Connected: WebSocketConnectionStatus
    data object Disconnected: WebSocketConnectionStatus
}