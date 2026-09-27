package com.techullurgy.howzapp.feature.chats.domain.impl.usecases

import com.techullurgy.howzapp.feature.chats.domain.api.repositories.ConversationRepository
import com.techullurgy.howzapp.feature.chats.domain.api.usecases.ObserveUnreadMessagesCountUseCase
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class DefaultObserveUnreadMessagesCountUseCase(
    private val conversationRepository: ConversationRepository
): ObserveUnreadMessagesCountUseCase {
    override fun invoke(conversationId: String): Flow<Int> {
        return conversationRepository.observeUnreadMessagesCount(conversationId)
    }
}