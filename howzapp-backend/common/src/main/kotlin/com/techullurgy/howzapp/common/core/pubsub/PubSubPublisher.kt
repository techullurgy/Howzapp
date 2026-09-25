package com.techullurgy.howzapp.common.core.pubsub

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.data.redis.core.ReactiveRedisTemplate

interface PubSubPublisher<in T> {
    fun publish(channel: String, message: T)
}

class RedisPubSubPublisher<T: Any>(
    private val redisTemplate: ReactiveRedisTemplate<String, T>,
    private val applicationScope: CoroutineScope
) : PubSubPublisher<T> {
    override fun publish(channel: String, message: T) {
        applicationScope.launch {
            redisTemplate
                .convertAndSend(channel, message)
                .awaitSingleOrNull()
        }
    }
}