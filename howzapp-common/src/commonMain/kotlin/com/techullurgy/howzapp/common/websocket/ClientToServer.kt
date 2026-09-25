package com.techullurgy.howzapp.common.websocket

import kotlinx.serialization.Serializable

@Serializable
sealed interface ClientToServer: WebSocketEvent