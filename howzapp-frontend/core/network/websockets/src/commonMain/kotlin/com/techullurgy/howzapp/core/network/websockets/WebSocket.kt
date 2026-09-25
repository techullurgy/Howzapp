package com.techullurgy.howzapp.core.network.websockets

interface WebSocket<Incoming, Outgoing> {
    fun obtainConnection(): WebSocketConnection<Incoming, Outgoing>
}