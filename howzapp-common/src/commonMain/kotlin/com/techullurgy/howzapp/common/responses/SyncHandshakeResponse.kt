package com.techullurgy.howzapp.common.responses

import kotlinx.serialization.Serializable

@Serializable
data class SyncHandshakeResponse(
    val conversations: List<String>, // Conversation Id(s) to trigger sync
)
