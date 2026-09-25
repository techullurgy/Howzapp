package com.techullurgy.howzapp.feature.chats.db.entities

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.techullurgy.howzapp.feature.chats.db.models.MessageAcksStored

@Entity
data class PendingMessageAcksEntity(
    @PrimaryKey val id: String,
    val conversationId: String,
    val messageId: String,
    val type: MessageAcksStored,
    val isDone: Boolean = false
)