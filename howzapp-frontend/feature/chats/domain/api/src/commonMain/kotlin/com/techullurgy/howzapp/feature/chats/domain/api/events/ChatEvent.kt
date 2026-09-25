package com.techullurgy.howzapp.feature.chats.domain.api.events

import kotlin.time.Instant

sealed interface ChatEvent {

    sealed interface Incoming: ChatEvent {
        data object SyncTriggerEvent: Incoming

        data class MessageUpdateEvent(
            val conversationId: String,
            val messageId: String
        ): Incoming

        data class ReceivedRequiredEvent(
            val conversationId: String,
            val messageId: String,
            val timestamp: Instant
        ): Incoming

        data class ReadRequiredEvent(
            val conversationId: String,
            val messageId: String,
            val timestamp: Instant
        ): Incoming
    }

    sealed interface Outgoing: ChatEvent
}