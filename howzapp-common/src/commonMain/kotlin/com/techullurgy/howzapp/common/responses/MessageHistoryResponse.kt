package com.techullurgy.howzapp.common.responses

import com.techullurgy.howzapp.common.dto.MessageResponseDto
import kotlinx.serialization.Serializable

@Serializable
data class MessageHistoryResponse(
    val conversationId: String,
    val messages: List<MessageResponseDto>
)
