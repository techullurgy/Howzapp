package com.techullurgy.howzapp.feature.chats.db.entities

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity
data class ConversationEntity(
    @PrimaryKey
    val id: String,
    val title: String,
)
