package com.techullurgy.howzapp.common.core.pubsub

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.listener.ChannelTopic

interface PubSubListener<out T> {
    fun listen(channel: String): Flow<T>
}

class RedisPubSubListener<T: Any>(
    private val redisTemplate: ReactiveRedisTemplate<String, T>,
): PubSubListener<T> {
    override fun listen(channel: String): Flow<T> {
        return redisTemplate.listenTo(ChannelTopic(channel))
            .asFlow()
            .map { it.message }
    }
}