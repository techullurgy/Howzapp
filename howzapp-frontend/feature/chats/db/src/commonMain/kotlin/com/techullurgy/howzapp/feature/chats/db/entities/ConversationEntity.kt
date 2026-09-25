package com.techullurgy.howzapp.feature.chats.db.entities

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.techullurgy.howzapp.feature.chats.db.models.ConversationTypeStored

@Entity
data class ConversationEntity(
    @PrimaryKey
    val id: String,
    val type: ConversationTypeStored,

    /* DIRECT RELATED COLUMNS */
    val to: String?,

    /* GROUP RELATED COLUMNS  */
    val title: String?,
    val avatarUrl: String?,
)
