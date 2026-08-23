package com.techullurgy.howzapp.base.network.websockets

import com.techullurgy.howzapp.core.network.websockets.WebsocketConnector
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession

inline fun <reified ServerToClientEvent, reified ClientToServerEvent> ktorWebsocketConnector(
    client: HttpClient,
): WebsocketConnector<ServerToClientEvent, ClientToServerEvent> {
    return WebsocketConnector {
        ktorWebsocketConnection<ServerToClientEvent, ClientToServerEvent>(
            openSession = { client.webSocketSession(it) }
        )
    }
}