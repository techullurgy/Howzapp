package com.techullurgy.howzapp.feature.chats.db.entities

import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.PrimaryKey
import com.techullurgy.howzapp.feature.chats.db.models.MessageUploadStatusStored

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = ConversationMessageOutboxEntity::class,
            parentColumns = ["id"],
            childColumns = ["batchId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ConversationEntity::class,
            parentColumns = ["id"],
            childColumns = ["conversationId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
    ]
)
data class MessageUploadsEntity(
    @PrimaryKey val uploadId: String,
    val conversationId: String,
    val batchId: String,
    val status: MessageUploadStatusStored,
)
