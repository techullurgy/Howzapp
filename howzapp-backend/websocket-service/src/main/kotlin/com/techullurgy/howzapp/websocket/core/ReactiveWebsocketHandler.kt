package com.techullurgy.howzapp.websocket.core

import com.techullurgy.howzapp.common.core.pubsub.IPubSubManager
import com.techullurgy.howzapp.common.core.pubsub.PubSubConstants
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono
import reactor.core.publisher.Sinks
import java.time.Duration
import java.time.Instant
import kotlin.time.Duration.Companion.seconds

@Component
class ReactiveWebSocketHandler(
    private val pubSubManager: IPubSubManager,
) : WebSocketHandler {

    override fun handle(session: WebSocketSession): Mono<Void> = mono {
        // 1. Extract userId from query param (e.g. /ws?userId=user_123)
        val userId = extractUserId(session) ?: run {
            session.close().awaitSingleOrNull()
            return@mono null
        }

        val userChannel = "${PubSubConstants.USER_CHANNEL_PREFIX}:$userId"
        val presenceKey = "${PubSubConstants.PRESENCE_CHANNEL_PREFIX}:$userId"
        val lastSeenKey = PubSubConstants.LAST_SEEN_INVALIDATION_CHANNEL

        val outgoingSink = Sinks.many().unicast().onBackpressureBuffer<String>()

        try {
            coroutineScope {
                launch {
                    pubSubManager.listenTo(userChannel)
                        .collect { outgoingSink.tryEmitNext(it) }
                }.invokeOnCompletion { cancel() }

                launch {
                    while(isActive) {
                        pubSubManager.valueSet(presenceKey, "ONLINE", Duration.ofSeconds(30))
                        delay(20.seconds)
                    }
                }.invokeOnCompletion { cancel() }

                // Incoming
                launch {
                    session.receive()
                        .asFlow()
                        .collect { frame ->
                            if (frame.type == WebSocketMessage.Type.TEXT) {
                                processIncomingFrame(
                                    userId,
                                    frame.payloadAsText
                                )
                            }
                        }
                }.invokeOnCompletion { cancel() }

                // Outgoing
                launch {
                    session.send(
                        outgoingSink.asFlux()
                            .map(session::textMessage)
                    ).awaitSingleOrNull()
                }.invokeOnCompletion { cancel() }
            }
        } finally {
            outgoingSink.tryEmitComplete()

            pubSubManager.valueDelete(presenceKey)

            pubSubManager.convertAndSend(
                lastSeenKey,
                "$userId#${Instant.now().toEpochMilli()}"
            )
        }

        null
    }

    private fun extractUserId(session: WebSocketSession): String? {
        return session.handshakeInfo.uri.query
            ?.split("&")
            ?.firstOrNull { it.startsWith("userId=") }
            ?.substringAfter("userId=")
    }

    private suspend fun processIncomingFrame(senderId: String, payloadJson: String) {
        // Handle incoming raw JSON (CHAT, ACK, PING)
    }
}