package com.techullurgy.howzapp.infra.sync.api

import com.techullurgy.howzapp.core.domain.AppConnectionState
import com.techullurgy.howzapp.core.domain.AppWebsocketEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface WebsocketManager<StoC, CtoS> {
    val connectionState: StateFlow<AppConnectionState>

    val incomingFlow: Flow<StoC>

    fun send(event: CtoS)
    fun retry()
}