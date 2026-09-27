package com.techullurgy.howzapp.feature.chats.domain.api.models

import kotlin.time.Instant

sealed interface Conversation {
    val conversationId: String
    data class Direct(
        override val conversationId: String,
        val to: String // UserId,
    ): Conversation

    data class Group(
        override val conversationId: String,
        val title: String,
        val avatarUrl: String?,
        val createdAt: Instant,
        val participants: List<ParticipantInfo>
    ): Conversation
}