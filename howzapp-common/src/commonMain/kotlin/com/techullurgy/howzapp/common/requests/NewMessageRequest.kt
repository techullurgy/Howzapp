package com.techullurgy.howzapp.common.requests

import com.techullurgy.howzapp.common.dto.MessageContentDto
import kotlinx.serialization.Serializable

@Serializable
data class NewMessageRequest(
    val senderId: String,
    val conversationId: String,
    val localMessageId: String,
    val messageContent: MessageContentDto
)