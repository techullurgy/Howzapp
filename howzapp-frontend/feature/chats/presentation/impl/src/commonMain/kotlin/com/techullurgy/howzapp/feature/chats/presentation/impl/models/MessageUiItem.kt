package com.techullurgy.howzapp.feature.chats.presentation.impl.models

import com.techullurgy.howzapp.feature.chats.domain.api.models.content.MessageContent
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.MessageReaction
import kotlin.time.Instant

data class MessageUiItem(
    val id: String,
    val conversationId: String,
    val seqNo: Long,
    val owner: OwnerMetadata,
    val content: MessageContent,
    val timestamp: Instant,
    val reactions: List<MessageReaction>,
    val replyTo: String?,
    val forwarded: Boolean,
    val edited: Boolean,
    val starred: Boolean,
    val deleted: Boolean,
)
