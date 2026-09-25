package com.techullurgy.howzapp.feature.chats.db.entities

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.techullurgy.howzapp.feature.chats.db.models.MessageUploadStatusStored

@Entity
data class MessageUploadsEntity(
    @PrimaryKey val uploadId: String,
    val conversationId: String,
    val batchId: String,
    val status: MessageUploadStatusStored,
)
