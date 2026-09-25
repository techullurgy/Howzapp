package com.techullurgy.howzapp.base.network.websockets

import com.techullurgy.howzapp.core.network.websockets.WebSocket
import com.techullurgy.howzapp.core.network.websockets.WebSocketConnection
import io.ktor.client.HttpClient

class KtorWebSocket<Incoming, Outgoing>(
    private val client: HttpClient,
    private val serialize: (String) -> Incoming,
    private val deserialize: (Outgoing) -> String,
): WebSocket<Incoming, Outgoing> {
    override fun obtainConnection(): WebSocketConnection<Incoming, Outgoing> {
        return KtorWebSocketConnection(
            client = client,
            serialize = serialize,
            deserialize = deserialize
        )
    }
}