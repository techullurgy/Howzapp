package com.techullurgy.howzapp.feature.chats.db.dao

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Transaction
import androidx.room3.Upsert
import com.techullurgy.howzapp.feature.chats.db.entities.ConversationEntity
import com.techullurgy.howzapp.feature.chats.db.entities.DirectConversationEntity
import com.techullurgy.howzapp.feature.chats.db.entities.GroupConversationEntity
import com.techullurgy.howzapp.feature.chats.db.entities.GroupConversationParticipantsCrossRef
import com.techullurgy.howzapp.feature.chats.db.relations.ConversationRelation
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationDao {

    @Transaction
    @Query("""SELECT * FROM ConversationEntity WHERE id = :conversationId""")
    fun observeConversation(conversationId: String): Flow<ConversationRelation?>

    @Upsert
    suspend fun upsertConversation(conversation: ConversationEntity)

    @Upsert
    suspend fun upsertDirectConversation(conversation: DirectConversationEntity)

    @Upsert
    suspend fun upsertGroupConversation(conversation: GroupConversationEntity)

    @Upsert
    suspend fun upsertGroupParticipants(vararg refs: GroupConversationParticipantsCrossRef)
}