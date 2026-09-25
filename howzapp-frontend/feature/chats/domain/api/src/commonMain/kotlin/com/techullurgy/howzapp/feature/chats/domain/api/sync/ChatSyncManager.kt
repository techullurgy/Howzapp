package com.techullurgy.howzapp.feature.chats.domain.api.sync

import com.techullurgy.howzapp.core.domain.AppConnectionState
import com.techullurgy.howzapp.feature.chats.domain.api.events.ChatEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ChatSyncManager {
    val connectionState: StateFlow<AppConnectionState>

    val events: Flow<ChatEvent.Incoming>

    fun send(event: ChatEvent.Outgoing)
}