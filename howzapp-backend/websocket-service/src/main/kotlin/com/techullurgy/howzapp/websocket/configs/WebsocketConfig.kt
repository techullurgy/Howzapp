package com.techullurgy.howzapp.websocket.configs

import com.techullurgy.howzapp.websocket.core.ReactiveWebSocketHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter

@Configuration
class WebsocketConfig(
    private val handler: ReactiveWebSocketHandler
) {
    /**
     * Map URL paths to reactive WebSocket handlers
     */
    @Bean
    fun websocketHandlerMapping(): HandlerMapping {
        val map = mapOf(
            "/ws" to handler
        )

        return SimpleUrlHandlerMapping(
            map,
            Ordered.HIGHEST_PRECEDENCE
        )
    }

    /**
    * Required by Spring WebFlux to execute WebSocket handshakes
    */
    @Bean
    fun webSocketHandlerAdapter(): WebSocketHandlerAdapter {
        return WebSocketHandlerAdapter()
    }
}