package com.techullurgy.howzapp.feature.chats.domain.api.usecases

import androidx.paging.PagingData
import com.techullurgy.howzapp.feature.chats.domain.api.models.ConversationMessage
import kotlinx.coroutines.flow.Flow

interface ObserveForPagedMessagesUseCase {
    operator fun invoke(conversationId: String, initialRefreshKey: Long): Flow<PagingData<ConversationMessage>>
}