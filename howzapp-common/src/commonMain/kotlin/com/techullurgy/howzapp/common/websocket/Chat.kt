package com.techullurgy.howzapp.common.websocket

import kotlinx.serialization.Serializable

@Serializable
sealed interface ChatBasedServerToClient: ServerToClient

@Serializable
sealed interface ChatBasedClientToServer: ClientToServer