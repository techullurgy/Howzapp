package com.techullurgy.howzapp.feature.chats.data.handlers

import com.techullurgy.howzapp.common.dto.MessageResponseDto
import com.techullurgy.howzapp.core.database.Database
import com.techullurgy.howzapp.feature.chats.data.handlers.tasks.LocalMessageIdUpdateTask
import com.techullurgy.howzapp.feature.chats.data.mappers.toConversationMessage
import com.techullurgy.howzapp.feature.chats.data.mappers.toConversationMessageEntity
import com.techullurgy.howzapp.feature.chats.data.repos.ConversationLocalRepository
import com.techullurgy.howzapp.feature.chats.domain.api.repositories.ConversationMutationRepository
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Singleton

@Singleton
internal class MessageResponseHandler(
    private val conversationLocalRepository: ConversationLocalRepository,
    private val conversationMutationRepository: ConversationMutationRepository,
    @Provided private val database: Database
) {
    suspend fun handle(
        response: MessageResponseDto,
    ) {
        database.withWriteTransaction {
            handleResponse(response)
        }
    }

    suspend fun handle(
        responses: List<MessageResponseDto>
    ) {
        database.withWriteTransaction {
            responses.forEach { handleResponse(it) }
        }
    }

    private suspend fun handleResponse(
        response: MessageResponseDto
    ) {
        var message = response.message.toConversationMessage().toConversationMessageEntity()

        val isDeliveredSyncNeeded = response.pendingSync?.isDeliveredSyncNeeded == true
        val isReadSyncNeeded = response.pendingSync?.isReadSyncNeeded == true

        if(isDeliveredSyncNeeded) {
            conversationMutationRepository.deliveryReceiptFor(message.conversation, message.id)
        }
        if(isReadSyncNeeded) {
            message = message.copy(isRead = false)
        }
        if(response.localMessageIdSync != null && response.localMessageIdSync != message.id) {
            LocalMessageIdUpdateTask(conversationLocalRepository, conversationMutationRepository, database).invoke(
                localMessageId = response.localMessageIdSync!!,
                serverMessageId = message.id
            )
        }
        conversationLocalRepository.saveMessage(message)
    }
}
