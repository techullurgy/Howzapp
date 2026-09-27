package com.techullurgy.howzapp.feature.chats.data.repos

import com.techullurgy.howzapp.feature.chats.domain.api.models.Conversation
import com.techullurgy.howzapp.feature.chats.domain.api.models.ConversationMessage
import com.techullurgy.howzapp.feature.chats.domain.api.models.OutboxMessage
import kotlinx.coroutines.flow.Flow

interface ConversationLocalRepository {

    suspend fun saveConversation(conversation: Conversation)
    suspend fun saveMessage(message: ConversationMessage)
    suspend fun saveMessages(messages: List<ConversationMessage>)

    fun observeForConversation(conversationId: String): Flow<Conversation?>
    fun observeConversationMessages(conversationId: String): Flow<List<ConversationMessage>>
    fun observeForOutboxMessagesInComplete(conversationId: String): Flow<List<OutboxMessage>>

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