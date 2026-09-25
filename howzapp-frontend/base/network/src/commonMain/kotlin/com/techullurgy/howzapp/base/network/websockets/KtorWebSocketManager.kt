package com.techullurgy.howzapp.base.network.websockets

import com.techullurgy.howzapp.core.network.http.NetworkRequestParams
import com.techullurgy.howzapp.core.network.websockets.WebSocketManager
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow

internal class KtorWebSocketManager<Incoming, Outgoing>(
    private val client: HttpClient,
    private val serialize: (String) -> Incoming,
    private val deserialize: (Outgoing) -> String,
): WebSocketManager<Incoming, Outgoing> {
    private val webSocket by lazy { KtorWebSocket(client, serialize, deserialize) }
    private val webSocketConnection by lazy { webSocket.obtainConnection() }

    override val connectionStatus by lazy { webSocketConnection.connectionStatus }

    override fun connectAndObserve(params: NetworkRequestParams): Flow<Incoming> {
        return webSocketConnection.incoming(params)
    }

    override fun send(message: Outgoing): Boolean {
        return webSocketConnection.outgoing(message)
    }
}