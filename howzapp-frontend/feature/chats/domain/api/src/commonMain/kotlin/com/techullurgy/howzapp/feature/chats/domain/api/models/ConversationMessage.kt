package com.techullurgy.howzapp.feature.chats.domain.api.models

import com.techullurgy.howzapp.feature.chats.domain.api.models.content.MessageContent
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.MessageReaction
import com.techullurgy.howzapp.feature.users.domain.api.models.UserId
import kotlin.time.Instant

data class ConversationMessage(
    val id: ConversationMessageId,
    val conversationId: ConversationId,
    val senderId: UserId,
    val content: MessageContent,
    val timestamp: Instant,
    val status: MessageDeliveryStatus?,
    val reactions: List<MessageReaction> = emptyList(),
    val replyTo: ConversationMessageId? = null,
    val forwarded: Boolean = false,
    val edited: Boolean = false,
    val starred: Boolean = false,
    val deleted: Boolean = false,
    val isRead: Boolean? = null
)
