package com.techullurgy.howzapp.common.requests

import kotlinx.serialization.Serializable

@Serializable
data class MessageHistoryRequest(
    val conversationId: String,
    val loadKey: Long,
    val loadSize: Int
)
