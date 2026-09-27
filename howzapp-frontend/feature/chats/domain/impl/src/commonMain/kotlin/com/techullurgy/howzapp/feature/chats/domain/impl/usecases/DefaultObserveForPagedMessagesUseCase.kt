package com.techullurgy.howzapp.feature.chats.domain.impl.usecases

import androidx.paging.PagingData
import com.techullurgy.howzapp.feature.chats.domain.api.models.ConversationMessage
import com.techullurgy.howzapp.feature.chats.domain.api.repositories.ConversationRepository
import com.techullurgy.howzapp.feature.chats.domain.api.usecases.ObserveForPagedMessagesUseCase
import kotlinx.coroutines.flow.Flow

class DefaultObserveForPagedMessagesUseCase(
    private val conversationRepository: ConversationRepository
): ObserveForPagedMessagesUseCase {
    override fun invoke(conversationId: String, initialRefreshKey: Long): Flow<PagingData<ConversationMessage>> {
        return conversationRepository.observeForPagedMessages(
            conversationId = conversationId,
            initialRefreshKey = initialRefreshKey
        )
    }
}