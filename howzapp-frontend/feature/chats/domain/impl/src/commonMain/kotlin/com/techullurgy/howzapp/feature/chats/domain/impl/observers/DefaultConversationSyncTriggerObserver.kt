@file:Suppress("unused")

package com.techullurgy.howzapp.feature.chats.domain.impl.observers

import com.techullurgy.howzapp.core.domain.AppConnectionState
import com.techullurgy.howzapp.feature.chats.domain.api.events.ChatEvent
import com.techullurgy.howzapp.feature.chats.domain.api.models.MessageAcks
import com.techullurgy.howzapp.feature.chats.domain.api.models.PendingMessageAcks
import com.techullurgy.howzapp.feature.chats.domain.api.observers.ConversationSyncTriggerObserver
import com.techullurgy.howzapp.feature.chats.domain.api.repositories.ConversationRepository
import com.techullurgy.howzapp.feature.chats.domain.api.sync.ChatSyncManager
import com.techullurgy.howzapp.feature.chats.domain.api.tasks.SaveMessagesToLocalDatabaseTask
import com.techullurgy.howzapp.feature.chats.domain.impl.tasks.ConversationsSyncWithServerTask
import com.techullurgy.howzapp.feature.chats.domain.impl.tasks.MessageReceiptsRequiredHandlerTask
import com.techullurgy.howzapp.feature.chats.domain.impl.tasks.MessageUpdatesHandlerTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Singleton

@Singleton
class DefaultConversationSyncTriggerObserver(
    @Provided private val scope: CoroutineScope,
    @Provided private val conversationRepository: ConversationRepository,
    @Provided private val chatSyncManager: ChatSyncManager,
    @Provided private val saveMessagesToLocalDatabaseTask: SaveMessagesToLocalDatabaseTask
): ConversationSyncTriggerObserver {

    private val observationFlow = chatSyncManager.events
        .onEach {
            when(it) {
                is ChatEvent.Incoming.MessageUpdateEvent -> {
                    MessageUpdatesHandlerTask(conversationRepository)
                        .invoke(
                            conversationId = it.conversationId,
                            messageId = it.messageId
                        )
                }
                is ChatEvent.Incoming.ReceivedRequiredEvent -> {
                    MessageReceiptsRequiredHandlerTask(conversationRepository)
                        .invoke(
                            ack = PendingMessageAcks(
                                id = "",
                                conversationId = it.conversationId,
                                messageId = it.messageId,
                                ack = MessageAcks.DeliveryReceipt
                            ),
                            timestamp = it.timestamp
                        )
                }
                is ChatEvent.Incoming.ReadRequiredEvent -> {
                    MessageReceiptsRequiredHandlerTask(conversationRepository)
                        .invoke(
                            ack = PendingMessageAcks(
                                id = "",
                                conversationId = it.conversationId,
                                messageId = it.messageId,
                                ack = MessageAcks.ReadReceipt
                            ),
                            timestamp = it.timestamp
                        )
                }
                ChatEvent.Incoming.SyncTriggerEvent -> {
                    ConversationsSyncWithServerTask(conversationRepository, saveMessagesToLocalDatabaseTask).invoke()
                }
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