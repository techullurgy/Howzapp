package com.techullurgy.howzapp.conversation.events

import com.techullurgy.howzapp.common.domain.ids.ConversationId

sealed interface ConversationEvent {
    data class SyncTriggerEvent(
        val conversationId: ConversationId,
    ): ConversationEvent
}