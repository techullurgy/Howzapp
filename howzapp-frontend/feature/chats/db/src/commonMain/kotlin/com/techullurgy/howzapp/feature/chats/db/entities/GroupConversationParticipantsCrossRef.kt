package com.techullurgy.howzapp.feature.chats.db.entities

import androidx.room3.Entity
import androidx.room3.ForeignKey
import com.techullurgy.howzapp.feature.chats.db.models.GroupParticipantTypeStored
import com.techullurgy.howzapp.feature.users.db.entities.UserEntity
import kotlin.time.Instant

@Entity(
    primaryKeys = ["conversationId", "userId"],
    foreignKeys = [
        ForeignKey(
            entity = GroupConversationEntity::class,
            parentColumns = ["conversationId"],
            childColumns = ["conversationId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.NO_ACTION,
        ),
    ]
)
data class GroupConversationParticipantsCrossRef(
    val conversationId: String,
    val userId: String,
    val joinedAt: Instant,
    val type: GroupParticipantTypeStored,
)