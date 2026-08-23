package com.techullurgy.howzapp.infra.sync.impl

import com.techullurgy.howzapp.core.domain.AppConnectionState
import com.techullurgy.howzapp.core.domain.AppWebsocketEvent
import com.techullurgy.howzapp.core.network.system.SystemNetworkObserver
import com.techullurgy.howzapp.core.network.system.SystemNetworkState
import com.techullurgy.howzapp.core.network.websockets.BaseWebsocketSource
import com.techullurgy.howzapp.core.network.websockets.WebsocketConnectionEvent
import com.techullurgy.howzapp.core.network.websockets.WebsocketConnector
import com.techullurgy.howzapp.core.session.UserSessionPreferences
import com.techullurgy.howzapp.infra.sync.api.WebsocketManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.shareIn
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Singleton

@Singleton
internal class WebsocketManagerImpl(
    @Provided sessionPreferences: UserSessionPreferences,
    @Provided systemNetworkObserver: SystemNetworkObserver,
    @Provided externalScope: CoroutineScope,
    @Provided websocketConnector: WebsocketConnector<String, String>
): WebsocketManager<String, String> {
    override val connectionState: StateFlow<AppConnectionState> field = MutableStateFlow<AppConnectionState>(AppConnectionState.Disconnected)

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

    private val incomingMessagesFlow = combine(
        sessionPreferences.observeAuthInfo(),
        systemNetworkObserver.networkState
        // App is in Foreground? (also need to add)
    ) { authUser, networkState ->
        if(authUser != null && networkState == SystemNetworkState.Connected) {
            authUser.accessToken
        } else null
    }
        .distinctUntilChanged()
        .flatMapLatest { token ->
            if(token == null) {
                connectionState.value = AppConnectionState.Disconnected
                emptyFlow()
            } else {
                createWebsocketFlow()
            }
        }

    private val source = with(websocketConnector) { baseWebsocketSourceFor("") }

    override val incomingFlow: Flow<String> = retryableIncomingMessagesFlow
        .mapNotNull {
            when(it) {
                is AppWebsocketEvent.Data<String> -> it.data
                AppWebsocketEvent.Failed -> null
            }
        }

    override fun send(event: String) {
        source.sendEvent(event)
    }

    override fun retry() {
        retrySignal.tryEmit(Unit)
    }

    private fun createWebsocketFlow(): Flow<AppWebsocketEvent<String>> = callbackFlow {
        source.observeUpdates().collect {
            when(it) {
                WebsocketConnectionEvent.Connected -> {
                    connectionState.value = AppConnectionState.Connected
                }
                WebsocketConnectionEvent.Connecting -> {
                    connectionState.value = AppConnectionState.Connecting
                }
                is WebsocketConnectionEvent.Data<String> -> {
                    send(AppWebsocketEvent.Data(it.data))
                }
                WebsocketConnectionEvent.Disconnected -> {
                    connectionState.value = AppConnectionState.Disconnected
                }
                is WebsocketConnectionEvent.Failed -> {
                    connectionState.value = AppConnectionState.Disconnected
                }
            }
        }
    }
}

context(connector: WebsocketConnector<String, String>)
private fun baseWebsocketSourceFor(url: String): BaseWebsocketSource<String, String, String, String> {
    return object: BaseWebsocketSource<String, String, String, String>(
        connector = connector,
        url = url
    ) {
        override fun mapIncoming(event: String): String {
            TODO("Not yet implemented")
        }

        override fun mapError(exception: Exception): String {
            TODO("Not yet implemented")
        }

        override fun mapOutgoing(event: String): String {
            TODO("Not yet implemented")
        }
    }
}