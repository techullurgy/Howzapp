package com.techullurgy.howzapp.common.core.pubsub

import com.techullurgy.howzapp.common.domain.ids.ConversationId
import kotlinx.serialization.Serializable

@Serializable
sealed interface PubSubEvent {
    @Serializable
    data class SyncTrigger(val conversationId: ConversationId): PubSubEvent
}