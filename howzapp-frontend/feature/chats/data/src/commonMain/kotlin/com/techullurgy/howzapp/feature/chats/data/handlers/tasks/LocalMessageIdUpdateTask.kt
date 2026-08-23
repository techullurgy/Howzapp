package com.techullurgy.howzapp.feature.chats.data.handlers.tasks

import com.techullurgy.howzapp.core.database.Database
import com.techullurgy.howzapp.feature.chats.data.repos.ConversationLocalRepository
import com.techullurgy.howzapp.feature.chats.domain.api.repositories.ConversationMutationRepository
import org.koin.core.annotation.Provided

internal class LocalMessageIdUpdateTask(
    private val conversationLocalRepository: ConversationLocalRepository,
    private val conversationMutationRepository: ConversationMutationRepository,
    @Provided private val database: Database
) {
    suspend operator fun invoke(
        localMessageId: String,
        serverMessageId: String
    ) {
        database.withWriteTransaction {
            conversationLocalRepository.updateMessageId(localMessageId, serverMessageId)
            conversationMutationRepository.updateMessageIds(localMessageId, serverMessageId)
        }
    }
}