package com.techullurgy.howzapp.conversation.db.repositories

import com.techullurgy.howzapp.common.domain.ids.ConversationId
import com.techullurgy.howzapp.common.domain.ids.UserId
import com.techullurgy.howzapp.conversation.domain.Conversation
import com.techullurgy.howzapp.conversation.domain.ConversationParticipant

interface ConversationRepository {
    suspend fun saveConversation(conversation: Conversation): Conversation

    suspend fun addParticipant(
        conversationId: ConversationId,
        participant: ConversationParticipant
    )

    suspend fun findAllConversationsFor(
        participant: UserId
    ): List<Conversation>

    suspend fun findSyncableConversationsFor(
        participant: UserId
    ): List<Conversation>

    suspend fun findConversation(conversationId: ConversationId): Conversation?

    suspend fun incrementUnreceivedMessagesCountFor(conversationId: ConversationId, user: UserId)
    suspend fun decrementUnreadMessagesCountFor(conversationId: ConversationId, user: UserId)
}