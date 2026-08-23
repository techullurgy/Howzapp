package com.techullurgy.howzapp.conversation.db.repositories

import com.techullurgy.howzapp.conversation.db.entities.ConversationParticipantEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.data.cassandra.core.ReactiveCassandraTemplate
import org.springframework.data.cassandra.core.query.Criteria
import org.springframework.data.cassandra.core.query.Query
import org.springframework.stereotype.Repository

@Repository
class ParticipantRepository(
    private val cassandraTemplate: ReactiveCassandraTemplate
) {

    fun findParticipants(conversationId: String): Flow<ConversationParticipantEntity> {
        val query = Query.query(Criteria.where("conversation_id").`is`(conversationId))
        return cassandraTemplate.select(query, ConversationParticipantEntity::class.java).asFlow()
    }

    suspend fun addParticipant(entity: ConversationParticipantEntity): ConversationParticipantEntity {
        return cassandraTemplate.insert(entity).awaitSingle()
    }
}