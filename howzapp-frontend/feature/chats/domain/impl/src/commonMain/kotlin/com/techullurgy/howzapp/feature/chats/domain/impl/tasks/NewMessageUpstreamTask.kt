package com.techullurgy.howzapp.feature.chats.domain.impl.tasks

import com.techullurgy.howzapp.feature.chats.domain.api.models.MessageOutboxEntry
import com.techullurgy.howzapp.feature.chats.domain.api.repositories.ConversationRepository


class NewMessageUpstreamTask(
    private val conversationRepository: ConversationRepository
) {
    /**
     * Try to Promote to Original Message with SENT Status
     */
    suspend operator fun invoke(entry: MessageOutboxEntry) {
        conversationRepository.sendMessage(entry.conversationId, entry.batchId, entry.payload)
            .onSuccess {
                conversationRepository.saveMessages(listOf(it))
            }
    }
}