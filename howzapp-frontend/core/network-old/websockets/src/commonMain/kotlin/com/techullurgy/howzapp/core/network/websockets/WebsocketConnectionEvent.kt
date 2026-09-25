package com.techullurgy.howzapp.core.network.websockets

import com.techullurgy.howzapp.core.domain.ErrorCode

sealed interface WebsocketConnectionEvent<out T> {
    data object Connecting: WebsocketConnectionEvent<Nothing>
    data object Connected: WebsocketConnectionEvent<Nothing>
    // Normal Completion
    data object Disconnected: WebsocketConnectionEvent<Nothing>
    // Abnormal Completion
    data class Failed(val error: ErrorCode, val throwable: Throwable?): WebsocketConnectionEvent<Nothing>

    data class Data<T>(val data: T): WebsocketConnectionEvent<T>
}