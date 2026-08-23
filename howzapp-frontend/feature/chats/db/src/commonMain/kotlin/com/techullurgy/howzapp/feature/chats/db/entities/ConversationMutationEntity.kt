package com.techullurgy.howzapp.feature.chats.db.entities

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.techullurgy.howzapp.feature.chats.db.models.ConversationMutationDataStored
import com.techullurgy.howzapp.feature.chats.db.models.ConversationMutationStatusStored
import com.techullurgy.howzapp.feature.chats.db.models.ConversationMutationTypeStored

@Entity
data class ConversationMutationEntity(
    @PrimaryKey val id: String,
    val conversation: String,
    val createdAt: Long,
    val type: ConversationMutationTypeStored,
    val data: ConversationMutationDataStored,
    val status: ConversationMutationStatusStored
)