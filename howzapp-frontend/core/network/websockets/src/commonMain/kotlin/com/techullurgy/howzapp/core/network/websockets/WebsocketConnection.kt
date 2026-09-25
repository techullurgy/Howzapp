package com.techullurgy.howzapp.core.network.websockets

import com.techullurgy.howzapp.core.network.http.NetworkRequestParams
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface WebSocketConnection<Incoming, Outgoing> {
    val connectionStatus: StateFlow<WebSocketConnectionStatus>

    fun incoming(params: NetworkRequestParams): Flow<Incoming>
    fun outgoing(message: Outgoing): Boolean
}