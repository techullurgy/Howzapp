package com.techullurgy.howzapp.feature.chats.domain.impl.usecases

import com.techullurgy.howzapp.feature.chats.domain.api.models.Conversation
import com.techullurgy.howzapp.feature.chats.domain.api.repositories.ConversationRepository
import com.techullurgy.howzapp.feature.chats.domain.api.usecases.ObserveConversationUseCase
import kotlinx.coroutines.flow.Flow

class DefaultObserveConversationUseCase(
    private val conversationRepository: ConversationRepository
): ObserveConversationUseCase {
    override fun invoke(conversationId: String): Flow<Conversation?> {
        return conversationRepository.observeForConversation(conversationId)
    }
}