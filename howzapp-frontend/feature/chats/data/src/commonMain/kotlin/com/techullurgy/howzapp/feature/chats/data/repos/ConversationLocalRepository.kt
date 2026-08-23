package com.techullurgy.howzapp.feature.chats.data.repos

import com.techullurgy.howzapp.feature.chats.domain.api.models.ConversationMessage
import kotlinx.coroutines.flow.Flow

interface ConversationLocalRepository {
    suspend fun saveMessage(entity: ConversationMessage)

    suspend fun updateMessageId(oldMessageId: String, newMessageId: String)

    fun observeConversationMessages(conversationId: String): Flow<List<ConversationMessage>>

    suspend fun getMessagesBefore(
        conversationId: String,
        currentTimestamp: Long,
        limit: Int
    ): List<ConversationMessage>

    suspend fun getMessagesAfter(
        conversationId: String,
        currentTimestamp: Long,
        limit: Int
    ): List<ConversationMessage>

    suspend fun getMessagesAround(
        conversationId: String,
        currentTimestamp: Long,
        limit: Int
    ): List<ConversationMessage>

    suspend fun getFirstUnreadMessageTimestamp(conversationId: String): Long?

    suspend fun getLatestTimestamp(conversationId: String): Long?

    suspend fun hasMessages(conversationId: String): Boolean
}