package com.techullurgy.howzapp.base.network.websockets

import com.techullurgy.howzapp.core.network.http.NetworkRequestParams
import com.techullurgy.howzapp.core.network.websockets.WebSocketConnection
import com.techullurgy.howzapp.core.network.websockets.WebSocketConnectionStatus
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class KtorWebSocketConnection<Incoming, Outgoing>(
    private val client: HttpClient,
    private val serialize: (String) -> Incoming,
    private val deserialize: (Outgoing) -> String,
): WebSocketConnection<Incoming, Outgoing> {
    override val connectionStatus: StateFlow<WebSocketConnectionStatus>
        field = MutableStateFlow<WebSocketConnectionStatus>(WebSocketConnectionStatus.Disconnected)

    private var outgoingChannel: Channel<Outgoing>? = null

    override fun incoming(
        params: NetworkRequestParams,
    ): Flow<Incoming> = channelFlow {
        connectionStatus.update { WebSocketConnectionStatus.Connecting }

        try {
            val session = client.webSocketSession(params.url)
            outgoingChannel = Channel(Channel.BUFFERED)

            connectionStatus.update { WebSocketConnectionStatus.Connected }

            coroutineScope {
                launch {
                    session
                        .incoming
                        .receiveAsFlow()
                        .collect { frame ->
                            when (frame) {
                                is Frame.Text -> {
                                    val receivedMessage = frame.readText()
                                    val serialized = serialize(receivedMessage)
                                    send(serialized)
                                }
                                is Frame.Close -> {}
                                else -> {}
                            }
                        }
                }.invokeOnCompletion { cancel() }

                launch {
                    outgoingChannel!!
                        .receiveAsFlow()
                        .collect {
                            session.send(Frame.Text(deserialize(it)))
                        }
                }.invokeOnCompletion { cancel() }
            }
        } finally {
            outgoingChannel?.close()
            outgoingChannel = null
            connectionStatus.update { WebSocketConnectionStatus.Disconnected }
        }
    }

    override fun outgoing(message: Outgoing): Boolean {
        return outgoingChannel?.trySend(message)?.isSuccess ?: false
    }
}