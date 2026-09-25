package com.techullurgy.howzapp.core.network.websockets

import kotlinx.coroutines.channels.ReceiveChannel

interface WebsocketConnection<ServerToClientEvent, ClientToServerEvent> {
    val incoming: ReceiveChannel<WebsocketConnectionEvent<ServerToClientEvent>>

    /**
     * It suspends until the websocket connection disrupts
     * */
    suspend fun connect(url: String)
    suspend fun send(value: ClientToServerEvent)
}