package com.techullurgy.howzapp.base.network.websockets

import com.techullurgy.howzapp.core.domain.ErrorCode
import com.techullurgy.howzapp.core.network.websockets.WebsocketConnection
import com.techullurgy.howzapp.core.network.websockets.WebsocketConnectionEvent
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.converter
import io.ktor.serialization.WebsocketContentConverter
import io.ktor.serialization.deserialize
import io.ktor.serialization.serialize
import io.ktor.websocket.Frame
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@PublishedApi
internal inline fun <reified ServerToClientEvent, reified ClientToServerEvent> ktorWebsocketConnection(
    noinline openSession: suspend (url: String) -> DefaultClientWebSocketSession
): WebsocketConnection<ServerToClientEvent, ClientToServerEvent> {
    return KtorWebsocketConnection(
        openSession = openSession,
        serialize = { serialize(it) },
        deserialize = { deserialize(it) }
    )
}

@PublishedApi
internal class KtorWebsocketConnection<ServerToClientEvent, ClientToServerEvent>(
    private val openSession: suspend (url: String) -> DefaultClientWebSocketSession,
    private val serialize: suspend WebsocketContentConverter.(ClientToServerEvent) -> Frame,
    private val deserialize: suspend WebsocketContentConverter.(Frame) -> ServerToClientEvent
) : WebsocketConnection<ServerToClientEvent, ClientToServerEvent> {
    override val incoming: ReceiveChannel<WebsocketConnectionEvent<ServerToClientEvent>> field = Channel(Channel.BUFFERED)

    private val mutex = Mutex()
    private var session: DefaultClientWebSocketSession? = null

    override suspend fun connect(url: String) {
        try {
            incoming.send(WebsocketConnectionEvent.Connecting)
            val newSession = openSession(url)

            mutex.withLock {
                session = newSession
            }

            val converter = newSession.converter ?: error("No converter configured")

            incoming.send(WebsocketConnectionEvent.Connected)
            for (frame in newSession.incoming) {
                if (frame is Frame.Text) {
                    incoming.send(
                        WebsocketConnectionEvent.Data(
                            data = converter.deserialize(frame)
                        )
                    )
                }
            }
            incoming.send(WebsocketConnectionEvent.Disconnected)
        } catch(e: Exception) {
            if(e !is CancellationException) {
                incoming.send(
                    WebsocketConnectionEvent.Failed(
                        error = ErrorCode.Unknown(e.message ?: "Unknown Error"),
                        throwable = e
                    )
                )
            }
            throw e
        } finally {
            mutex.withLock {
                session?.cancel()
                session = null
            }
            incoming.close()
        }
    }

    override suspend fun send(value: ClientToServerEvent) {
        val currentSession = mutex.withLock { session }
            ?: error("Cannot send: Websocket is not connected")

        val converter = currentSession.converter
            ?: error("Cannot send: No converter configured")

        currentSession.outgoing.send(converter.serialize(value))
    }
}