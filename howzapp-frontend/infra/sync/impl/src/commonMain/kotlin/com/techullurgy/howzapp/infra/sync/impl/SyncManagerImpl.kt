@file:Suppress("unused")

package com.techullurgy.howzapp.infra.sync.impl

import com.techullurgy.howzapp.common.websocket.ClientToServer
import com.techullurgy.howzapp.common.websocket.ServerToClient
import com.techullurgy.howzapp.core.network.http.NetworkRequestMethod
import com.techullurgy.howzapp.core.network.http.NetworkRequestParams
import com.techullurgy.howzapp.core.network.system.SystemNetworkObserver
import com.techullurgy.howzapp.core.network.system.SystemNetworkState
import com.techullurgy.howzapp.core.network.websockets.WebSocketConnectionStatus
import com.techullurgy.howzapp.core.network.websockets.WebSocketManager
import com.techullurgy.howzapp.core.session.UserSessionPreferences
import com.techullurgy.howzapp.infra.sync.api.SyncConnectionStatus
import com.techullurgy.howzapp.infra.sync.api.SyncManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Singleton

@Singleton
internal class SyncManagerImpl(
    @Provided sessionPreferences: UserSessionPreferences,
    @Provided systemNetworkObserver: SystemNetworkObserver,
    @Provided externalScope: CoroutineScope,
    @Provided private val webSocketManager: WebSocketManager<ServerToClient, ClientToServer>
): SyncManager<ServerToClient, ClientToServer> {
    override val connectionStatus: StateFlow<SyncConnectionStatus> =
        webSocketManager.connectionStatus
            .map { it.toSyncConnectionStatus() }
            .stateIn(
                scope = externalScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = SyncConnectionStatus.Disconnected
            )

    private val incomingMessagesFlow = combine(
        sessionPreferences.observeAuthInfo(),
        systemNetworkObserver.networkState
        // TODO: App is in Foreground? (also need to add)
    ) { authInfo, networkState ->
        authInfo != null && networkState == SystemNetworkState.Connected
    }
        .distinctUntilChanged()
        .flatMapLatest { canConnect ->
            if(canConnect) {
                createWebsocketFlow()
            } else {
                emptyFlow()
            }
        }

    private val retrySignal = MutableSharedFlow<Unit>()

    private val retryableIncomingMessagesFlow = merge(
        flowOf(Unit),
        retrySignal
    )
        .flatMapLatest { incomingMessagesFlow }
        .shareIn(
            scope = externalScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    override val incomingFlow: Flow<ServerToClient> = retryableIncomingMessagesFlow

    override fun send(event: ClientToServer) {
        webSocketManager.send(event)
    }

    override fun retry() {
        retrySignal.tryEmit(Unit)
    }

    private fun createWebsocketFlow(): Flow<ServerToClient> {
        val params = NetworkRequestParams.WithoutBody(
            url = "ws://....",
            method = NetworkRequestMethod.GET
        )
        return webSocketManager.connectAndObserve(params)
    }
}

private fun WebSocketConnectionStatus.toSyncConnectionStatus() = when(this) {
    WebSocketConnectionStatus.Connected -> SyncConnectionStatus.Connected
    WebSocketConnectionStatus.Connecting -> SyncConnectionStatus.Connecting
    WebSocketConnectionStatus.Disconnected -> SyncConnectionStatus.Disconnected
}