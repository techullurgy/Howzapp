package com.techullurgy.howzapp.feature.chats.domain.api.repositories

import com.techullurgy.howzapp.feature.chats.domain.api.models.ConversationMutation
import kotlinx.coroutines.flow.Flow

interface ConversationMutationRepository {
    /**
     * Expose only PENDING mutations
     */
    fun pendingToSyncMutationsOrdered(): Flow<List<ConversationMutation>>

    /**
     * Expose only CANCELLED mutations
     */
    fun cancelledMutations(): Flow<List<ConversationMutation>>

    suspend fun markSyncingIfPending(mutationId: String): Boolean

    suspend fun markSynced(mutationId: String): Boolean

    suspend fun deliveryReceiptFor(conversationId: String, messageId: String)

    suspend fun updateMessageIds(oldMessageId: String, newMessageId: String)
}

/*
When the UI cancels an upload, perform a single transaction:

UPDATE conversation_mutations
SET status = 'CANCELLED'
WHERE id = :mutationId;

UPDATE conversation_mutations
SET status = 'PAUSED'
WHERE conversation = :conversationId
  AND sequence > :sequence
  AND status IN ('PENDING', 'FAILED');


 */