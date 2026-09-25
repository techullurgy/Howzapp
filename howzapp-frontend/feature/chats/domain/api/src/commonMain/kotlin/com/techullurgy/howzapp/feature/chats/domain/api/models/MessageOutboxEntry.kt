package com.techullurgy.howzapp.feature.chats.domain.api.models

import com.techullurgy.howzapp.feature.chats.domain.api.models.content.MessageContent
import kotlin.time.Instant

data class MessageOutboxEntry(
    val id: String,
    val conversationId: String,
    val batchId: String,
    val timestamp: Instant,
    val payload: MessageContent,
    val status: MessageOutboxStatus,
    val updateTime: Instant
)
