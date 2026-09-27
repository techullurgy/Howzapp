package com.techullurgy.howzapp.feature.chats.domain.api.models

import kotlin.time.Instant

data class OutboxMessage(
    val conversationId: String,
    val batchId: String,
    val timestamp: Instant,
    val payload: OutboxMessageContent
)
