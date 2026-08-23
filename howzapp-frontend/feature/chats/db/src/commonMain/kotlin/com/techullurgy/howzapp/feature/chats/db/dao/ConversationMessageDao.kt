package com.techullurgy.howzapp.feature.chats.db.dao

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert
import com.techullurgy.howzapp.feature.chats.db.entities.ConversationMessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationMessageDao {
    @Query("""
        SELECT *
        FROM ConversationMessageEntity
        WHERE conversation = :conversationId
    """)
    fun observeConversationMessages(conversationId: String): Flow<List<ConversationMessageEntity>>

    @Upsert
    suspend fun upsert(message: ConversationMessageEntity)

    @Upsert
    suspend fun upsertAll(messages: List<ConversationMessageEntity>)

    @Query("""DELETE FROM ConversationMessageEntity WHERE conversation = :conversationId""")
    suspend fun clearConversation(conversationId: String)

    @Query("""
        UPDATE ConversationMessageEntity
        SET serverId = :serverId, 
            seqId = :serverSeqId, 
            status = 'SENT', 
            createdAt = :serverTimestamp, 
            updatedAt = (strftime('%s','now') * 1000)
        WHERE id = :localId
    """)
    suspend fun promoteToSentMessage(
        localId: String,
        serverId: String,
        serverSeqId: Int,
        serverTimestamp: String,
    )

    /**
     * Older messages (smaller timestamp)
     *
     * Example:
     * currentKey = 100
     *
     * Returns:
     * 99, 98, 97...
     */
    @Query("""
        SELECT *
        FROM ConversationMessageEntity
        WHERE conversation = :conversationId
          AND createdAt < :currentTimestamp
        ORDER BY createdAt DESC
        LIMIT :limit
    """)
    suspend fun getMessagesBefore(
        conversationId: String,
        currentTimestamp: Long,
        limit: Int
    ): List<ConversationMessageEntity>


    /**
     * Newer messages (greater timestamp)
     *
     * Example:
     * currentKey = 100
     *
     * Returns:
     * 101, 102, 103...
     */
    @Query("""
        SELECT *
        FROM ConversationMessageEntity
        WHERE conversation = :conversationId
          AND createdAt > :currentTimestamp
        ORDER BY createdAt ASC
        LIMIT :limit
    """)
    suspend fun getMessagesAfter(
        conversationId: String,
        currentTimestamp: Long,
        limit: Int
    ): List<ConversationMessageEntity>

    @Query("""
        SELECT createdAt FROM ConversationMessageEntity 
        WHERE conversation = :conversationId AND status != 'READ' 
        ORDER BY createdAt ASC LIMIT 1
    """)
    suspend fun getFirstUnreadTimestamp(conversationId: String): Long?

    @Query("""
        SELECT createdAt FROM ConversationMessageEntity 
        WHERE conversation = :conversationId
        ORDER BY createdAt DESC LIMIT 1
    """)
    suspend fun getLatestTimestamp(conversationId: String): Long?

    @Query("""
        SELECT createdAt FROM ConversationMessageEntity
        WHERE conversation = :conversationId AND status = 'DELIVERED'
        ORDER BY createdAt DESC LIMIT 1
    """)
    suspend fun getFirstUnreadMessageTimestamp(conversationId: String): Long?

    @Query("""SELECT EXISTS(SELECT 1 FROM ConversationMessageEntity WHERE conversation = :conversationId LIMIT 1)""")
    suspend fun hasMessages(conversationId: String): Boolean
}