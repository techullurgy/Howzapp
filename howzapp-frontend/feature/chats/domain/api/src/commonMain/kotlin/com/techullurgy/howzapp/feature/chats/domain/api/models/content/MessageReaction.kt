package com.techullurgy.howzapp.feature.chats.domain.api.models.content

import com.techullurgy.howzapp.feature.users.domain.api.models.UserId
import kotlin.time.Instant

data class MessageReaction(
    val emoji: String,
    val userId: UserId,
    val timestamp: Instant
)
