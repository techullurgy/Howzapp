package com.techullurgy.howzapp.feature.chats.domain.api.models.content

import com.techullurgy.howzapp.feature.users.domain.api.models.UserId

/**
 * Examples:
 * 1) User joined
 * 2) User left
 * 3) Group icon changed
 * 4) Encryption enabled
 * 5) Admin changed
 */
data class SystemMessage(
    val event: SystemEvent
) : MessageContent

sealed interface SystemEvent {
    data class UserJoined(
        val userId: UserId
    ) : SystemEvent

    data class UserLeft(
        val userId: UserId
    ) : SystemEvent

    data class GroupNameChanged(
        val oldName: String,
        val newName: String
    ) : SystemEvent

    data class GroupIconChanged(
        val iconUrl: String
    ) : SystemEvent

    data class AdminPromoted(
        val userId: UserId
    ) : SystemEvent

    data class AdminRemoved(
        val userId: UserId
    ) : SystemEvent

    data object EndToEndEncryptionEnabled : SystemEvent
}