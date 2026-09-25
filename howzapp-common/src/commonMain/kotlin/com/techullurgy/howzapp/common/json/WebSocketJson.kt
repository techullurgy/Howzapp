package com.techullurgy.howzapp.common.json

import com.techullurgy.howzapp.common.websocket.ClientToServer
import com.techullurgy.howzapp.common.websocket.ServerToClient
import com.techullurgy.howzapp.common.websocket.WebSocketEvent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@OptIn(ExperimentalSerializationApi::class)
val WebSocketJson = Json {
    serializersModule = SerializersModule {
        polymorphic(WebSocketEvent::class) {
            subclassesOfSealed<ServerToClient>()
            subclassesOfSealed<ClientToServer>()
        }
    }
}