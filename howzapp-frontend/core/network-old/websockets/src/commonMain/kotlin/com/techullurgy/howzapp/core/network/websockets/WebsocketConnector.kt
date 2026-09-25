package com.techullurgy.howzapp.core.network.websockets

fun interface WebsocketConnector<ServerToClientEvent, ClientToServerEvent> {
    suspend fun newConnection(url: String): WebsocketConnection<ServerToClientEvent, ClientToServerEvent>
}
