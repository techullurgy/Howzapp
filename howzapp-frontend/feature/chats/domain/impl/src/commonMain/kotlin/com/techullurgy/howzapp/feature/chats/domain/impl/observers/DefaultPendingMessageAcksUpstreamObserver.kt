@file:Suppress("unused")

package com.techullurgy.howzapp.feature.chats.domain.impl.observers

import com.techullurgy.howzapp.core.domain.AppConnectionState
import com.techullurgy.howzapp.core.utils.bufferUniqueSequential
import com.techullurgy.howzapp.feature.chats.domain.api.observers.PendingMessageAcksUpstreamObserver
import com.techullurgy.howzapp.feature.chats.domain.api.repositories.ConversationRepository
import com.techullurgy.howzapp.feature.chats.domain.api.sync.ChatSyncManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Singleton

@Singleton
internal class DefaultPendingMessageAcksUpstreamObserver(
    @Provided private val scope: CoroutineScope,
    @Provided private val conversationRepository: ConversationRepository,
    @Provided private val chatSyncManager: ChatSyncManager
): PendingMessageAcksUpstreamObserver {

    private val observationFlow = conversationRepository.findIncompletedPendingMessageAcks()
        .bufferUniqueSequential { it.id }
        .onEach { ack ->
            conversationRepository.sendPendingMessageAck(ack)
                .onSuccess {
                    conversationRepository.markPendingMessageAckAsComplete(ack.id)
                }
        }

    override fun observe() {
        chatSyncManager.connectionState
            .flatMapLatest {
                if(it == AppConnectionState.Connected) {
                    observationFlow
                } else emptyFlow()
            }
            .launchIn(scope)
    }
}