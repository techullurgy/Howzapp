package com.techullurgy.howzapp.feature.chats.db.dao

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Transaction
import com.techullurgy.howzapp.feature.chats.db.relations.OutboxMessageRelation
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationMessageOutboxDao {

    @Transaction
    @Query("""
        SELECT * 
        FROM ConversationMessageOutboxEntity 
        WHERE conversationId = :conversationId' 
               AND status <> 'COMPLETED'
    """)
    fun observeOutboxMessagesInComplete(conversationId: String): Flow<List<OutboxMessageRelation>>
}