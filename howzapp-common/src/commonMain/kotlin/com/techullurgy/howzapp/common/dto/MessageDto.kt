package com.techullurgy.howzapp.common.dto

import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    val messageId: String,
    val conversationId: String,
    val senderId: String,
    val content: MessageContentDto,
    val timestamp: Long,
    val reactions: List<MessageReactionDto> = emptyList(),
    val replyTo: String? = null,
    val forwarded: Boolean,
    val edited: Boolean,
    val starred: Boolean,
    val deleted: Boolean,
    val status: MessageDeliveryStatusDto?
)

enum class MessageDeliveryStatusDto {
    SENT, DELIVERED, READ
}

@Serializable
data class MessageReactionDto(
    val emoji: String,
    val userId: String,
    val timestamp: Long
)