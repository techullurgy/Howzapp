package com.techullurgy.howzapp.common.configs

import com.techullurgy.howzapp.common.core.pubsub.IPubSubManager
import com.techullurgy.howzapp.common.core.pubsub.RedisPubSubManager
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer

@Configuration
@ConditionalOnBooleanProperty(value = ["app.redis.enabled"], havingValue = true, matchIfMissing = false)
class RedisConfig {
    @Bean
    fun reactiveStringRedisTemplate(
        factory: ReactiveRedisConnectionFactory
    ): ReactiveStringRedisTemplate {
        return ReactiveStringRedisTemplate(factory)
    }

    @Bean
    fun reactiveRedisMessageListenerContainer(
        factory: ReactiveRedisConnectionFactory
    ): ReactiveRedisMessageListenerContainer {
        return ReactiveRedisMessageListenerContainer(factory)
    }

    @Bean
    @ConditionalOnClass(ReactiveStringRedisTemplate::class)
    fun redisPubSubManager(
        reactiveStringRedisTemplate: ReactiveStringRedisTemplate,
        reactiveRedisMessageListenerContainer: ReactiveRedisMessageListenerContainer
    ): IPubSubManager {
        return RedisPubSubManager(reactiveStringRedisTemplate, reactiveRedisMessageListenerContainer)
    }
}