package com.techullurgy.howzapp.feature.chats.domain.impl.tasks

import com.techullurgy.howzapp.feature.chats.domain.api.models.Conversation
import com.techullurgy.howzapp.feature.chats.domain.api.repositories.ConversationRepository
import com.techullurgy.howzapp.feature.chats.domain.api.tasks.SaveMessagesToLocalDatabaseTask
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.supervisorScope

class ConversationsSyncWithServerTask(
    private val conversationRepository: ConversationRepository,
    private val saveMessagesToLocalDatabaseTask: SaveMessagesToLocalDatabaseTask
) {
    suspend operator fun invoke() {
        conversationRepository.syncHandshake()
            .onSuccess { conversationIds ->
                supervisorScope {
                    val deferreds = conversationIds.map { conversationId ->
                        async { syncConversation(conversationId) }
                    }

                    deferreds.awaitAll()
                }
            }
    }

    private suspend fun syncConversation(conversationId: String) {
        val lastMessageSeqNo = conversationRepository.obtainLastMessageSeqNo(conversationId) ?: -1L
        conversationRepository.syncConversationFromServer(conversationId, lastMessageSeqNo)
            .onSuccess { messages ->
                saveMessagesToLocalDatabaseTask(
                    // TODO: Conversation needs to correctly built from network response.
                    Conversation.Direct(""),
                    messages
                )
            }
    }
}