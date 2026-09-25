package com.techullurgy.howzapp.infra.sync.api

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface SyncManager<Incoming, Outgoing> {
    val connectionStatus: StateFlow<SyncConnectionStatus>
    val incomingFlow: Flow<Incoming>
    fun send(event: Outgoing)
    fun retry()
}