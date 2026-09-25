package com.techullurgy.howzapp.feature.chats.domain.api.models

sealed interface Conversation {
    data class Direct(
        val to: String // UserId,
    ): Conversation

    data class Group(
        val title: String,
        val avatarUrl: String?
    ): Conversation
}