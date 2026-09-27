package com.techullurgy.howzapp.common.responses

import com.techullurgy.howzapp.common.dto.MessageDto
import kotlinx.serialization.Serializable

@Serializable
data class MessageHistoryResponse(
    val conversationId: String,
    val history: List<MessageDto>,
)
