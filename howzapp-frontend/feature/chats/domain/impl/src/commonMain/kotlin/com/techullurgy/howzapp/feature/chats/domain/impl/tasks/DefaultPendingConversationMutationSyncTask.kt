package com.techullurgy.howzapp.feature.chats.domain.impl.tasks

import com.techullurgy.howzapp.feature.chats.domain.api.models.ConversationMutation
import com.techullurgy.howzapp.feature.chats.domain.api.models.ConversationMutationData
import com.techullurgy.howzapp.feature.chats.domain.api.repositories.ConversationMutationRepository
import com.techullurgy.howzapp.feature.chats.domain.api.tasks.PendingConversationMutationSyncTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Singleton

@Singleton(binds = [PendingConversationMutationSyncTask::class])
internal class DefaultPendingConversationMutationSyncTask(
    private val newMessageSyncTask: NewMessageSyncTask,
    private val readReceiptSyncTask: ReadReceiptSyncTask,
    private val deliveryReceiptSyncTask: DeliveryReceiptSyncTask,
    @Provided private val mutationsRepository: ConversationMutationRepository
): PendingConversationMutationSyncTask {
    private val workers = mutableMapOf<String, ConversationWorker>()

    override suspend operator fun invoke() = coroutineScope {
        launch { observePendingMutationsAndReact() }
        launch { observeCancelledMutationsAndReact() }
        Unit
    }

    context(scope: CoroutineScope)
    private suspend fun observePendingMutationsAndReact() {
        mutationsRepository.pendingToSyncMutationsOrdered()
            .collect { mutations ->
                mutations.forEach { mutation ->
                    val claimed = mutationsRepository.markSyncingIfPending(mutation.id)
                    if (!claimed) return@forEach

                    val worker = workers.getOrPut(mutation.conversation) {
                        createWorker(scope = scope)
                    }

                    worker.channel.send(mutation)
                }
            }
    }

    private suspend fun observeCancelledMutationsAndReact() {
        mutationsRepository.cancelledMutations()
            .collect {
                it.forEach { mutation ->
                    workers.remove(mutation.conversation)
                        ?.job
                        ?.cancel()
                }
            }
    }

    private fun createWorker(
        scope: CoroutineScope
    ): ConversationWorker {
        val channel = Channel<ConversationMutation>(Channel.UNLIMITED)

        val job = scope.launch {
            for(mutation in channel) {
                processMutation(mutation)
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

    private suspend fun processMutation(
        mutation: ConversationMutation
    ) {
        try {
            // Actual Processing
            when(val data = mutation.data) {
                is ConversationMutationData.DeliveryReceipt -> deliveryReceiptSyncTask(data)
                is ConversationMutationData.NewMessage -> newMessageSyncTask(data)
                is ConversationMutationData.ReadReceipt -> readReceiptSyncTask(data)
            }

            mutationsRepository.markSynced(mutation.id)
        } catch (_: Exception) {
            // mutationsRepository.markFailedIfSyncing(mutation.id)
        }
    }

    private data class ConversationWorker(
        val channel: Channel<ConversationMutation>,
        val job: Job
    )
}