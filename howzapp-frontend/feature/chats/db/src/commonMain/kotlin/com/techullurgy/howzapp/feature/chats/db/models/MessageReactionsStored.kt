package com.techullurgy.howzapp.feature.chats.db.models

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class MessageReactionsStored(
    val reactions: List<MessageReactionStored>
)

@Serializable
data class MessageReactionStored(
    val emoji: String,
    val userId: String,
    val timestamp: Instant
)