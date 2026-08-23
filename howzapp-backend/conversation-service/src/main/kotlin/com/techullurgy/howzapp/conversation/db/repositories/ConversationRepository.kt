package com.techullurgy.howzapp.conversation.db.repositories

import com.techullurgy.howzapp.conversation.db.entities.ConversationCounterEntity
import com.techullurgy.howzapp.conversation.db.entities.MessageEntity
import com.techullurgy.howzapp.conversation.db.entities.UserConversationEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.data.cassandra.core.ReactiveCassandraTemplate
import org.springframework.data.cassandra.core.query.Criteria
import org.springframework.data.cassandra.core.query.Query
import org.springframework.data.cassandra.core.query.Update
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository
import kotlin.jvm.java

@Repository
class ConversationRepository(
    private val cassandraTemplate: ReactiveCassandraTemplate
) {

    suspend fun incrementAndGetSeqId(conversationId: String): Long {
        val query = Query.query(Criteria.where("conversation_id").`is`(conversationId))
        val current = cassandraTemplate.selectOne(query, ConversationCounterEntity::class.java).awaitSingleOrNull()

        val nextSeqId = (current?.lastSeqId ?: 0L) + 1L

        val updatedEntity = ConversationCounterEntity(
            conversationId = conversationId,
            lastSeqId = nextSeqId
        )

        cassandraTemplate.insert(updatedEntity).awaitSingle()
        return nextSeqId
    }

    suspend fun getLastSeqId(conversationId: String): Long {
        val query = Query.query(Criteria.where("conversation_id").`is`(conversationId))
        val current = cassandraTemplate.selectOne(query, ConversationCounterEntity::class.java).awaitSingleOrNull()
        return current?.lastSeqId ?: 0L
    }

    suspend fun saveMessage(message: MessageEntity): MessageEntity {
        return cassandraTemplate.insert(message).awaitSingle()
    }

    suspend fun updateMessageStatus(conversationId: String, seqId: Long, status: String) {
        val query = Query.query(
            Criteria.where("conversation_id").`is`(conversationId),
            Criteria.where("seq_id").`is`(seqId)
        )
        val update = Update.empty().set("status", status)
        cassandraTemplate.update(query, update, MessageEntity::class.java).awaitSingleOrNull()
    }

    suspend fun updateUserConvMeta(userId: String, conversationId: String, seqId: Long, isDirect: Boolean) {
        val userConv = UserConversationEntity(
            userId = userId,
            conversationId = conversationId,
            isDirect = isDirect,
            lastSeqId = seqId,
            unreadCount = 0
        )
        cassandraTemplate.insert(userConv).awaitSingle()
    }

    fun findMessagesSinceSeqId(conversationId: String, sinceSeqId: Long, limit: Int): Flow<MessageEntity> {
        val query = Query.query(
            Criteria.where("conversation_id").`is`(conversationId),
            Criteria.where("seq_id").gt(sinceSeqId)
        )
            .sort(Sort.by(Sort.Direction.ASC, "seq_id"))
            .limit(limit.toLong())

        return cassandraTemplate.select(query, MessageEntity::class.java).asFlow()
    }

    fun findDirectConversationIdsForUser(userId: String): Flow<String> {
        val query = Query.query(
            Criteria.where("user_id").`is`(userId),
            Criteria.where("is_direct").`is`(true)
        )
        return cassandraTemplate.select(query, UserConversationEntity::class.java)
            .map { it.conversationId }
            .asFlow()
    }
}