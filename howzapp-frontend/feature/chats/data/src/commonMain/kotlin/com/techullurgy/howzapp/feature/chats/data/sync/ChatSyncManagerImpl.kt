@file:Suppress("unused")

package com.techullurgy.howzapp.feature.chats.data.sync

import com.techullurgy.howzapp.common.websocket.ChatBasedServerToClient
import com.techullurgy.howzapp.common.websocket.ClientToServer
import com.techullurgy.howzapp.common.websocket.ServerToClient
import com.techullurgy.howzapp.core.domain.AppConnectionState
import com.techullurgy.howzapp.feature.chats.domain.api.events.ChatEvent
import com.techullurgy.howzapp.feature.chats.domain.api.sync.ChatSyncManager
import com.techullurgy.howzapp.infra.sync.api.SyncConnectionStatus
import com.techullurgy.howzapp.infra.sync.api.SyncManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Singleton

@Singleton
internal class ChatSyncManagerImpl(
    @Provided private val syncManager: SyncManager<ServerToClient, ClientToServer>,
    @Provided externalScope: CoroutineScope
): ChatSyncManager {
    override val connectionState: StateFlow<AppConnectionState> =
        syncManager.connectionStatus
            .map {
                when(it) {
                    SyncConnectionStatus.Connected -> AppConnectionState.Connected
                    SyncConnectionStatus.Connecting -> AppConnectionState.Connecting
                    SyncConnectionStatus.Disconnected -> AppConnectionState.Disconnected
                }
            }
            .stateIn(
                scope = externalScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = AppConnectionState.Disconnected
            )

    override val events: Flow<ChatEvent.Incoming> =
        syncManager.incomingFlow
            .filterIsInstance<ChatBasedServerToClient>()
            .map { it.toChatEventIncoming() }

    override fun send(event: ChatEvent.Outgoing) {
        syncManager.send(event.toChatBasedClientToServer())
    }
}