package com.techullurgy.howzapp.core.network.websockets

import com.techullurgy.howzapp.core.network.http.NetworkRequestParams
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface WebSocketManager<Incoming, Outgoing> {
    val connectionStatus: StateFlow<WebSocketConnectionStatus>

    fun connectAndObserve(params: NetworkRequestParams): Flow<Incoming>
    fun send(message: Outgoing): Boolean
}