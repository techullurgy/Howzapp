package com.techullurgy.howzapp.feature.chats.domain.api.repositories

import androidx.paging.PagingData
import com.techullurgy.howzapp.feature.chats.domain.api.models.ConversationMessage
import kotlinx.coroutines.flow.Flow

interface ConversationRepository {
    fun observeForMessages(
        conversationId: String,
        initialRefreshKey: Long
    ): Flow<PagingData<ConversationMessage>>

    suspend fun obtainFirstUnreadMessageTimestamp(conversationId: String): Long?
    suspend fun obtainOriginalMessage(conversationId: String, messageId: String): ConversationMessage?
}