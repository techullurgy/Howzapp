package com.techullurgy.howzapp.feature.chats.domain.api.models

data class PendingMessageAcks(
    val id: String,
    val conversationId: String,
    val messageId: String,
    val ack: MessageAcks
)
