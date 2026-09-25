package com.techullurgy.howzapp.websocket.core

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

class AppWebsocketHandler(): WebSocketHandler {
    override fun handle(session: WebSocketSession): Mono<Void> = mono {
        val userId = extractUserId(session).awaitSingleOrNull() ?: return@mono session.close().awaitSingleOrNull()

        try {
            coroutineScope {

            }
        } finally {

        }

        TODO("Not yet implemented")
    }

    private fun extractUserId(session: WebSocketSession): Mono<String> {
        return session.handshakeInfo.principal.map { it.name }
    }
}

//class UserChannelSubscriber(
//    private val template: ReactiveRedisMessageListenerContainer
//) {
//
//}