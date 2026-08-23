package com.techullurgy.howzapp.feature.chats.domain.impl.tasks

import com.techullurgy.howzapp.feature.chats.domain.api.models.ConversationMutationData
import com.techullurgy.howzapp.feature.chats.domain.api.repositories.ConversationRepository
import com.techullurgy.howzapp.feature.common.domain.api.services.FileUploadManager
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Singleton

@Singleton
internal class NewMessageSyncTask(
    @Provided private val fileUploadManager: FileUploadManager,
    private val conversationRepository: ConversationRepository
) {
    suspend operator fun invoke(
        message: ConversationMutationData.NewMessage
    ) {
        val originalMessage = conversationRepository.obtainOriginalMessage(
            conversationId = message.conversationId,
            messageId = message.messageId
        ) ?: throw IllegalStateException("No original message to sync")


    }
}