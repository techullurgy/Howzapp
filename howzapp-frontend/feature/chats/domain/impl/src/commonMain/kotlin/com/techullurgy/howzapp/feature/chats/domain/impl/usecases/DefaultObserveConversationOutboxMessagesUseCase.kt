package com.techullurgy.howzapp.feature.chats.domain.impl.usecases

import com.techullurgy.howzapp.feature.chats.domain.api.models.OutboxMessage
import com.techullurgy.howzapp.feature.chats.domain.api.repositories.ConversationRepository
import com.techullurgy.howzapp.feature.chats.domain.api.usecases.ObserveConversationOutboxMessagesUseCase
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
internal class DefaultObserveConversationOutboxMessagesUseCase(
    @Provided private val conversationRepository: ConversationRepository
): ObserveConversationOutboxMessagesUseCase {
    override fun invoke(conversationId: String): Flow<List<OutboxMessage>> {
        return conversationRepository.observeForOutboxMessagesInComplete(conversationId)
    }
}