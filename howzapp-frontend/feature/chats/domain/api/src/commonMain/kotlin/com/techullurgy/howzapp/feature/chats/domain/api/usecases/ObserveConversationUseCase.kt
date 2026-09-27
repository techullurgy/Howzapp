package com.techullurgy.howzapp.feature.chats.domain.api.usecases

import com.techullurgy.howzapp.feature.chats.domain.api.models.Conversation
import kotlinx.coroutines.flow.Flow

interface ObserveConversationUseCase {
    operator fun invoke(conversationId: String): Flow<Conversation?>
}