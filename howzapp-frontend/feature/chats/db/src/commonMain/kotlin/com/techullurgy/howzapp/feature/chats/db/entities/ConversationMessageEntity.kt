package com.techullurgy.howzapp.feature.chats.db.entities

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.techullurgy.howzapp.feature.chats.db.models.MessageContentStored
import com.techullurgy.howzapp.feature.chats.db.models.MessageContentTypeStored
import com.techullurgy.howzapp.feature.chats.db.models.MessageDeliveryStatusStored
import com.techullurgy.howzapp.feature.chats.db.models.MessageReactionsStored

@Entity
data class ConversationMessageEntity(
    @PrimaryKey
    val id: String,
    val conversation: String,
    val seqId: Int,
    val createdAt: Long,
    val updatedAt: Long,
    val senderId: String,
    val type: MessageContentTypeStored,
    val content: MessageContentStored,
    val status: MessageDeliveryStatusStored?,
    val reactions: MessageReactionsStored,
    val replyTo: String?,
    val forwarded: Boolean,
    val edited: Boolean,
    val starred: Boolean,
    val deleted: Boolean,
    val isRead: Boolean?
)

/*
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
    val deleted: Boolean = false
 */