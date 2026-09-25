package com.techullurgy.howzapp.feature.chats.db.entities

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.techullurgy.howzapp.feature.chats.db.models.MessageContentStored
import com.techullurgy.howzapp.feature.chats.db.models.MessageOutboxStatusStored
import kotlin.time.Clock
import kotlin.time.Instant

@Entity
data class ConversationMessageOutboxEntity(
    @PrimaryKey val id: String,
    val conversationId: String,
    val messageId: String,
    val timestamp: Instant,
    val payload: MessageContentStored,
    val status: MessageOutboxStatusStored,
    val updateTime: Instant = Clock.System.now()
)
