package com.techullurgy.howzapp.common.responses

import com.techullurgy.howzapp.common.dto.MessageDto
import com.techullurgy.howzapp.common.dto.MessagePendingSyncDto
import kotlinx.serialization.Serializable

@Serializable
data class SyncConversationResponse(
    val conversationId: String,
    val messages: List<MessageDto>,
    val pendingSyncDto: List<MessagePendingSyncDto>
)
