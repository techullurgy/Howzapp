package com.techullurgy.howzapp.common.requests

import com.techullurgy.howzapp.common.dto.MessageContentDto
import kotlinx.serialization.Serializable

@Serializable
data class NewMessageRequest(
    val conversationId: String,
    val localBatchId: String,
    val messageContent: MessageContentDto
)