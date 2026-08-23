package com.techullurgy.howzapp.status.service

import com.techullurgy.howzapp.common.dto.CreateStatusRequest
import com.techullurgy.howzapp.common.dto.InternalStatusDto
import com.techullurgy.howzapp.common.dto.StatusResponse
import com.techullurgy.howzapp.status.db.entities.StatusEntity
import com.techullurgy.howzapp.status.db.repository.StatusRepository
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class StatusService(
    private val statusRepository: StatusRepository
) {

    /**
     * Publish a new 24-hour status story
     */
    suspend fun postStatus(userId: String, request: CreateStatusRequest): StatusResponse {
        val now = Instant.now()
        val entity = StatusEntity(
            userId = userId,
            createdAt = now,
            statusId = "stat_${UUID.randomUUID().toString().replace("-", "").take(12)}",
            mediaUrl = request.mediaUrl,
            caption = request.caption,
            expiresAt = now.plusSeconds(86400) // 24-hour expiration
        )

        val saved = statusRepository.saveStatus(entity)
        return saved.toResponse()
    }

    /**
     * Get active non-expired statuses for a given set of contacts (Internal & Public)
     */
    suspend fun getActiveContactStatuses(contactUserIds: List<String>, sinceTimestamp: Long = 0): List<InternalStatusDto> {
        if (contactUserIds.isEmpty()) return emptyList()

        val now = Instant.now()
        val sinceInstant = if (sinceTimestamp > 0) Instant.ofEpochMilli(sinceTimestamp) else Instant.EPOCH

        return statusRepository.findStatusesByUserIds(contactUserIds)
            .filter { entity -> entity.expiresAt.isAfter(now) && entity.createdAt.isAfter(sinceInstant) }
            .map { entity ->
                InternalStatusDto(
                    userId = entity.userId,
                    statusId = entity.statusId,
                    mediaUrl = entity.mediaUrl,
                    caption = entity.caption,
                    createdAt = entity.createdAt,
                    expiresAt = entity.expiresAt
                )
            }
            .toList()
    }

    /**
     * Fetch active stories for a specific user
     */
    suspend fun getUserStatuses(userId: String): List<StatusResponse> {
        val now = Instant.now()
        return statusRepository.findUserStatuses(userId)
            .filter { entity -> entity.expiresAt.isAfter(now) }
            .map { entity -> entity.toResponse() }
            .toList()
    }

    private fun StatusEntity.toResponse() = StatusResponse(
        userId = this.userId,
        statusId = this.statusId,
        mediaUrl = this.mediaUrl,
        caption = this.caption,
        createdAt = this.createdAt,
        expiresAt = this.expiresAt
    )
}