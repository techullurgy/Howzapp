package com.techullurgy.howzapp.feature.chats.domain.api.usecases

import com.techullurgy.howzapp.feature.chats.domain.api.models.OutboxMessage
import kotlinx.coroutines.flow.Flow

interface ObserveConversationOutboxMessagesUseCase {
    operator fun invoke(conversationId: String): Flow<List<OutboxMessage>>
}