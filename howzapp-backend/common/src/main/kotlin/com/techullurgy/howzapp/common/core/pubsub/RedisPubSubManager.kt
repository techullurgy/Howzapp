package com.techullurgy.howzapp.common.core.pubsub

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer
import java.time.Duration

internal class RedisPubSubManager(
    private val redisTemplate: ReactiveStringRedisTemplate,
    private val listener: ReactiveRedisMessageListenerContainer
): IPubSubManager {
    override fun listenTo(key: String): Flow<String> {
        return redisTemplate.listenTo(ChannelTopic(key))
            .asFlow()
            .map { it.message }
    }

    override fun receive(keys: List<String>): Flow<String> {
        val topics = keys.map { ChannelTopic(it) }.toTypedArray()
        return listener.receive(*topics).asFlow().map { it.message }
    }

    override suspend fun valueSet(key: String, value: String, timeout: Duration?): Boolean? {
        return if (timeout == null) {
            redisTemplate.opsForValue().set(key, value).awaitSingleOrNull()
        } else {
            redisTemplate.opsForValue().set(key, value, timeout).awaitSingleOrNull()
        }
    }

    override suspend fun convertAndSend(destination: String, message: String): Long? {
        return redisTemplate.convertAndSend(destination, message).awaitSingleOrNull()
    }

    override suspend fun valueDelete(key: String): Boolean? {
        return redisTemplate.opsForValue().delete(key).awaitSingleOrNull()
    }

    override suspend fun setAdd(key: String, values: Set<String>): Long? {
        return redisTemplate.opsForSet().add(key, *values.toTypedArray()).awaitSingleOrNull()
    }

    override suspend fun setContains(key: String, value: String): Boolean? {
        return redisTemplate.opsForSet().isMember(key, value).awaitSingleOrNull()
    }

    override suspend fun setMembers(key: String): List<String>? {
        return redisTemplate.opsForSet().members(key).collectList().awaitSingleOrNull()
    }

    override suspend fun valueGet(key: String): String? {
        return redisTemplate.opsForValue().get(key).awaitSingleOrNull()
    }

    override suspend fun hashGet(key: String, hashKey: String): String? {
        return redisTemplate.opsForHash<String, String>()
            .get(key, hashKey)
            .awaitSingleOrNull()
    }

    override suspend fun hashPut(key: String, hashKey: String, value: String): Boolean? {
        return redisTemplate.opsForHash<String, String>()
            .put(key, hashKey, value)
            .awaitSingleOrNull()
    }

    override suspend fun hashPutAll(key: String, hashes: Map<String, String>): Boolean? {
        return redisTemplate.opsForHash<String, String>()
            .putAll(key, hashes)
            .awaitSingleOrNull()
    }

    override suspend fun hashEntries(key: String): Map<String, String>? {
        return redisTemplate.opsForHash<String, String>()
            .entries(key)
            .collectMap({ it.key }, { it.value })
            .awaitSingleOrNull()
    }
}