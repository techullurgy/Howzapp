package com.techullurgy.howzapp.feature.chats.presentation.impl.models

import com.techullurgy.howzapp.feature.users.domain.api.models.User
import kotlin.time.Instant

sealed interface ConversationUiItem {
    val conversationId: String

    data class Direct(
        override val conversationId: String,
        val to: User
    ): ConversationUiItem

    data class Group(
        override val conversationId: String,
        val title: String,
        val avatarUrl: String?,
        val createdAt: Instant,
        val participants: List<ParticipantInfoUiItem>
    ): ConversationUiItem
}