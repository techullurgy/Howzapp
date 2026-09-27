package com.techullurgy.howzapp.common.requests

import kotlinx.serialization.Serializable

@Serializable
data class SyncConversationRequest(
    val conversationId: String,
    val lastSeqNo: Long,
    val loadSize: Int
)
