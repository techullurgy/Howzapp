package com.techullurgy.howzapp.status.db.repository

import com.techullurgy.howzapp.status.db.entities.StatusEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.data.cassandra.core.ReactiveCassandraTemplate
import org.springframework.data.cassandra.core.query.Criteria
import org.springframework.data.cassandra.core.query.Query
import org.springframework.stereotype.Repository

@Repository
class StatusRepository(
    private val cassandraTemplate: ReactiveCassandraTemplate
) {

    suspend fun saveStatus(entity: StatusEntity): StatusEntity {
        return cassandraTemplate.insert(entity).awaitSingle()
    }

    /**
     * Range query for contact status feeds
     */
    fun findStatusesByUserIds(userIds: List<String>): Flow<StatusEntity> {
        val query = Query.query(Criteria.where("user_id").`in`(userIds))
        return cassandraTemplate.select(query, StatusEntity::class.java).asFlow()
    }

    /**
     * Query single user active statuses
     */
    fun findUserStatuses(userId: String): Flow<StatusEntity> {
        val query = Query.query(Criteria.where("user_id").`is`(userId))
        return cassandraTemplate.select(query, StatusEntity::class.java).asFlow()
    }
}