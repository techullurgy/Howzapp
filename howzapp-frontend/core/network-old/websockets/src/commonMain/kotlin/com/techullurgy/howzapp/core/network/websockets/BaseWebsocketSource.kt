package com.techullurgy.howzapp.core.network.websockets

import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

abstract class BaseWebsocketSource<ServerToClientRemote, ClientToServerRemote, ServerToClientDomain, ClientToServerDomain>(
    private val connector: WebsocketConnector<ServerToClientRemote, ClientToServerRemote>,
    private val url: String,
) {
    private val outgoingFlow = MutableSharedFlow<ClientToServerDomain>(extraBufferCapacity = 100)

    protected abstract fun mapIncoming(event: ServerToClientRemote): ServerToClientDomain
    protected abstract fun mapError(exception: Exception): ServerToClientDomain
    protected abstract fun mapOutgoing(event: ClientToServerDomain): ClientToServerRemote

    private val connectionFlow: Flow<WebsocketConnectionEvent<ServerToClientDomain>> = channelFlow {
        try {
            val connection = connector.newConnection(url)

            coroutineScope {
                launch {
                    connection.connect(url)
                }.invokeOnCompletion { cancel() }

                launch {
                    outgoingFlow.collect { domainEvent ->
                        connection.send(mapOutgoing(domainEvent))
                    }
                }.invokeOnCompletion { cancel() }

                // Iterate over incoming channel safely
                for (remoteEvent in connection.incoming) {
                    send(
                        when(remoteEvent) {
                            is WebsocketConnectionEvent.Data<ServerToClientRemote> -> {
                                WebsocketConnectionEvent.Data(mapIncoming(remoteEvent.data))
                            }
                            WebsocketConnectionEvent.Connected -> WebsocketConnectionEvent.Connected
                            WebsocketConnectionEvent.Connecting -> WebsocketConnectionEvent.Connecting
                            WebsocketConnectionEvent.Disconnected -> WebsocketConnectionEvent.Disconnected
                            is WebsocketConnectionEvent.Failed -> WebsocketConnectionEvent.Failed(remoteEvent.error, remoteEvent.throwable)
                        }
                    )
                }
                // THE FIX: If the server cleanly closes the connection,
                // the `for` loop finishes. We MUST cancel the scope here
                // to kill the infinite outgoingFlow child!
                cancel()
            }
        } catch(e: CancellationException) {
            throw e
        } catch (e: Exception) {
            currentCoroutineContext().ensureActive()
            send(WebsocketConnectionEvent.Data(mapError(e)))
        }
    }

    fun sendEvent(event: ClientToServerDomain) {
        outgoingFlow.tryEmit(event)
    }

    fun observeUpdates(): Flow<WebsocketConnectionEvent<ServerToClientDomain>> = connectionFlow
}