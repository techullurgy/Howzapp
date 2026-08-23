package com.techullurgy.howzapp.root.sync

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class ChatSyncService {

}

class PendingMutationsSyncTask(
    private val websocketManager: WebsocketManager,
    private val chatDao: ChatDao
) {
    suspend operator fun invoke() = coroutineScope {

        val workers = mutableMapOf<String, SendChannel<ConversationMutationsEntity>>()

        chatDao.pendingToSyncMutationsOrdered()
            .collect { mutations ->

                mutations.forEach { mutation ->

                    val claimed =
                        chatDao.markSyncingIfPending(mutation.id) == 1

                    if (!claimed) return@forEach

                    val worker = workers.getOrPut(mutation.chatId) {
                        createWorker(scope = this)
                    }

                    worker.send(mutation)
                }
            }
    }

    private fun createWorker(
        scope: CoroutineScope
    ): SendChannel<ConversationMutationsEntity> {
        return Channel<ConversationMutationsEntity>(Channel.UNLIMITED)
            .also { channel ->
                scope.launch {
                    for (mutation in channel) {
                        processMutation(mutation)
                    }
                }.invokeOnCompletion { channel.close() }
            }
    }

    private suspend fun processMutation(
        mutation: ConversationMutationsEntity
    ) {
        try {
            // Actual Processing
            websocketManager.send(mutation.toString())

            // Processing Done
            // chatDao.markSynced(mutation.id)

        } catch (e: Exception) {
            // chatDao.markPending(mutation.id)
        }
    }
}

interface ChatDao {
    fun pendingToSyncMutationsOrdered(): Flow<List<ConversationMutationsEntity>>
}

data class ConversationEntity(
    val id: String,
    val title: String,
)

data class MessageEntity(
    val id: String,
    val syncedId: String,
    val seqId: Int,
    val createdAt: Long,
    val type: MessageType,
    val content: String, // JSON
    val status: MessageStatus
)

enum class MessageStatus {
    PENDING, SENT, RECEIVED, READ
}

enum class MessageType {
    Text, Image, Video, Audio, RecordedAudio, Location,
}

data class ConversationMutationsEntity(
    val id: String,
    val chatId: String,
    val createdAt: Long,
    val mutationType: ConversationMutationType,
    val data: ConversationMutationData, //
    val isDone: Boolean
)

enum class ConversationMutationType {
    NewMessage, DeliveryReceipt, ReadReceipt,
}

sealed interface ConversationMutationData {
    data class NewMessage(
        val chatId: String,
        val messageId: String
    ): ConversationMutationData

    data class DeliveryReceipt(
        val chatId: String,
        val messageId: String
    ): ConversationMutationData

    data class ReadReceipt(
        val chatId: String,
        val messageId: String
    ): ConversationMutationData
}

interface WebsocketManager {
    fun send(message: String)
}

private fun <T, K> Flow<Iterable<T>>.uniqueAndFlat(keySelector: (T) -> K): Flow<T> {
    val uniqueSet = mutableSetOf<K>()
    return flow {
        collect { iterable ->
            iterable.forEach { value ->
                val key = keySelector(value)
                if(uniqueSet.add(key)) {
                    emit(value)
                }
            }
        }
    }
}