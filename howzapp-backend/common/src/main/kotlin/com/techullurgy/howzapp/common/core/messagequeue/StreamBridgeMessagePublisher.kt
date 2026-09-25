package com.techullurgy.howzapp.common.core.messagequeue

import org.springframework.cloud.stream.function.StreamBridge

class StreamBridgeMessagePublisher<T>(
    private val bridge: StreamBridge
): MessagePublisher<T> {
    override fun publish(binding: String, message: T) {
        bridge.send(binding, message)
    }
}