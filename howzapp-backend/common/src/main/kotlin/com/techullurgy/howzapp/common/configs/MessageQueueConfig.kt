package com.techullurgy.howzapp.common.configs

import com.techullurgy.howzapp.common.core.messagequeue.MessagePublisher
import com.techullurgy.howzapp.common.core.messagequeue.StreamBridgeMessagePublisher
import kotlinx.serialization.json.Json
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.converter.KotlinSerializationJsonMessageConverter
import org.springframework.messaging.converter.MessageConverter

@Configuration
class MessageQueueConfig {

    @Bean
    fun messagePublisher(bridge: StreamBridge): MessagePublisher<Any> {
        return StreamBridgeMessagePublisher(bridge)
    }

    @Bean
    fun messageConverter(json: Json): MessageConverter {
        return KotlinSerializationJsonMessageConverter(json)
    }
}