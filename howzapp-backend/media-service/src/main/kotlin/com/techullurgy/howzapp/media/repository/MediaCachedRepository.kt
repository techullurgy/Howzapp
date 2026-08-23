package com.techullurgy.howzapp.media.repository

import com.github.benmanes.caffeine.cache.Caffeine
import com.techullurgy.howzapp.common.core.pubsub.IPubSubManager
import com.techullurgy.howzapp.common.core.pubsub.PubSubConstants
import com.techullurgy.howzapp.media.db.entities.MediaMetadataEntity
import com.techullurgy.howzapp.media.db.repository.MediaMetadataEntityRepository
import com.techullurgy.howzapp.media.models.MediaMetadata
import org.springframework.stereotype.Repository
import java.time.Duration

@Repository
class MediaCachedRepository(
    private val pubSubManager: IPubSubManager,
    private val mediaMetadataEntityRepository: MediaMetadataEntityRepository
) {
    // --- L1 Caffeine Cache ---
    private val mediaL1Cache = Caffeine.newBuilder()
        .expireAfterWrite(Duration.ofMinutes(15))
        .maximumSize(100_000)
        .build<String, MediaMetadata>()

    // --- Upload Token Storage (Ephemeral 15-min TTL in Redis) ---
    suspend fun saveUploadToken(token: String, metadataJson: String) {
        pubSubManager.valueSet("${PubSubConstants.UPLOAD_TOK_PREFIX}$token", metadataJson, Duration.ofMinutes(15))
    }

    suspend fun getUploadTokenData(token: String): String? {
        return pubSubManager.valueGet("${PubSubConstants.UPLOAD_TOK_PREFIX}$token")
    }

    suspend fun deleteUploadToken(token: String) {
        pubSubManager.valueDelete("${PubSubConstants.UPLOAD_TOK_PREFIX}$token")
    }

    // --- Media Metadata Operations (L1 Caffeine -> L2 Redis -> L3 Cassandra) ---
    suspend fun saveMediaMetadata(entity: MediaMetadataEntity) {
        // 1. Persist to Apache Cassandra
        mediaMetadataEntityRepository.save(entity)

        val metadata = MediaMetadata(
            uniqueKey = entity.uniqueKey,
            originalUrl = entity.originalUrl,
            ownerUserId = entity.ownerUserId,
            purpose = entity.purpose,
            isClaimed = entity.isClaimed
        )

        // 2. Write-through to L2 Redis Hash
        val redisKey = "${PubSubConstants.REDIS_MEDIA_PREFIX}${entity.uniqueKey}"
        pubSubManager.hashPutAll(redisKey, mapOf(
            "originalUrl" to entity.originalUrl,
            "ownerUserId" to entity.ownerUserId,
            "purpose" to entity.purpose,
            "isClaimed" to entity.isClaimed.toString()
        ))

        // 3. Write-through to L1 Caffeine
        mediaL1Cache.put(entity.uniqueKey, metadata)
    }

    suspend fun findMediaMetadata(uniqueKey: String): MediaMetadata? {
        // 1. Check L1 Caffeine
        mediaL1Cache.getIfPresent(uniqueKey)?.let { return it }

        // 2. Check L2 Redis
        val redisKey = "${PubSubConstants.REDIS_MEDIA_PREFIX}$uniqueKey"
        val hash = pubSubManager.hashEntries(redisKey)

        if (!hash.isNullOrEmpty()) {
            val metadata = MediaMetadata(
                uniqueKey = uniqueKey,
                originalUrl = hash["originalUrl"] ?: "",
                ownerUserId = hash["ownerUserId"] ?: "",
                purpose = hash["purpose"] ?: "",
                isClaimed = hash["isClaimed"]?.toBoolean() ?: false
            )
            mediaL1Cache.put(uniqueKey, metadata)
            return metadata
        }

        // 3. Fallback to L3 Cassandra DB
        val cassandraEntity = mediaMetadataEntityRepository.findById(uniqueKey) ?: return null
        val metadata = MediaMetadata(
            uniqueKey = cassandraEntity.uniqueKey,
            originalUrl = cassandraEntity.originalUrl,
            ownerUserId = cassandraEntity.ownerUserId,
            purpose = cassandraEntity.purpose,
            isClaimed = cassandraEntity.isClaimed
        )

        // Populate L1 and L2 caches on miss
        mediaL1Cache.put(uniqueKey, metadata)
        pubSubManager.hashPutAll(redisKey, mapOf(
            "originalUrl" to cassandraEntity.originalUrl,
            "ownerUserId" to cassandraEntity.ownerUserId,
            "purpose" to cassandraEntity.purpose,
            "isClaimed" to cassandraEntity.isClaimed.toString()
        ))

        return metadata
    }
}