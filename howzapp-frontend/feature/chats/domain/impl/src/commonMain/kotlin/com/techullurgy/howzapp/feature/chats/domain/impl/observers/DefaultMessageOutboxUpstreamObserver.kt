@file:Suppress("unused")

package com.techullurgy.howzapp.feature.chats.domain.impl.observers

import com.techullurgy.howzapp.core.domain.AppConnectionState
import com.techullurgy.howzapp.core.utils.bufferPrioritySequential
import com.techullurgy.howzapp.feature.chats.domain.api.models.MessageOutboxEntry
import com.techullurgy.howzapp.feature.chats.domain.api.models.MessageOutboxStatus
import com.techullurgy.howzapp.feature.chats.domain.api.observers.MessageOutboxUpstreamObserver
import com.techullurgy.howzapp.feature.chats.domain.api.repositories.ConversationRepository
import com.techullurgy.howzapp.feature.chats.domain.api.sync.ChatSyncManager
import com.techullurgy.howzapp.feature.chats.domain.api.tasks.MessageFileUploadTask
import com.techullurgy.howzapp.feature.chats.domain.impl.tasks.NewMessageUpstreamTask
import com.techullurgy.howzapp.feature.chats.domain.impl.tasks.PendingToReadyOutboxConverterTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Singleton

@Singleton(binds = [MessageOutboxUpstreamObserver::class])
internal class DefaultMessageOutboxUpstreamObserver(
    @Provided private val conversationRepository: ConversationRepository,
    @Provided private val scope: CoroutineScope,
    @Provided private val messageFileUploadTask: MessageFileUploadTask,
    private val chatSyncManager: ChatSyncManager,
): MessageOutboxUpstreamObserver {
    private val workers = mutableMapOf<String, ConversationWorker>()

    private val observationFlow = conversationRepository.findIncompletedOutboxEntries()
        .bufferPrioritySequential(
            keySelector = { it.conversationId + it.batchId + it.updateTime.toString() },
            comparator = compareBy { it.timestamp }
        )
        .onEach { entry ->
            val worker = workers.getOrPut(entry.conversationId) {
                createWorker(scope = scope)
            }

            worker.channel.send(entry)
        }

    override fun observe() {
        chatSyncManager.connectionState
            .flatMapLatest {
                if(it is AppConnectionState.Connected) {
                    observationFlow
                } else emptyFlow()
            }
            .launchIn(scope)
    }

    private fun createWorker(
        scope: CoroutineScope
    ): ConversationWorker {
        val channel = Channel<MessageOutboxEntry>(Channel.UNLIMITED)

        val job = scope.launch {
            for(entry in channel) {
                processEntry(entry)
            }
        }

        job.invokeOnCompletion {
            channel.close()
        }

        return ConversationWorker(
            channel = channel,
            job = job
        )
    }

    private suspend fun processEntry(
        entry: MessageOutboxEntry
    ) {
        try {
            if(entry.status == MessageOutboxStatus.PENDING) {
                PendingToReadyOutboxConverterTask(
                    conversationRepository = conversationRepository,
                    messageFileUploadTask = messageFileUploadTask
                ).invoke(entry)
            }

            NewMessageUpstreamTask(conversationRepository).invoke(entry)

            conversationRepository.markOutboxEntryAsComplete(entry.id)
        } catch (e: Exception) {
            // TODO: Mark Failed if Necessary
            currentCoroutineContext().ensureActive()
            e.printStackTrace()
        }
    }

    private data class ConversationWorker(
        val channel: Channel<MessageOutboxEntry>,
        val job: Job
    )
}
