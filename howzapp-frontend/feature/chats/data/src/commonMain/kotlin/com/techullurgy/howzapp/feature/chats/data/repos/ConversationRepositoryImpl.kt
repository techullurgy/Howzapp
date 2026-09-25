@file:Suppress("unused")

package com.techullurgy.howzapp.feature.chats.data.repos

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.techullurgy.howzapp.core.database.Database
import com.techullurgy.howzapp.feature.chats.data.paging.ConversationPagingSource
import com.techullurgy.howzapp.feature.chats.data.paging.ConversationRemoteMediator
import com.techullurgy.howzapp.feature.chats.domain.api.models.ConversationMessage
import com.techullurgy.howzapp.feature.chats.domain.api.models.MessageAcks
import com.techullurgy.howzapp.feature.chats.domain.api.models.MessageOutboxEntry
import com.techullurgy.howzapp.feature.chats.domain.api.models.MessageUpload
import com.techullurgy.howzapp.feature.chats.domain.api.models.MessageUploadStatus
import com.techullurgy.howzapp.feature.chats.domain.api.models.PendingMessageAcks
import com.techullurgy.howzapp.feature.chats.domain.api.repositories.ConversationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Singleton

@Singleton
internal class ConversationRepositoryImpl(
    @Provided private val conversationLocalRepository: ConversationLocalRepository,
    private val conversationApi: ConversationApi,
    @Provided private val coroutineScope: CoroutineScope,
    @Provided private val database: Database
): ConversationRepository {
    override suspend fun saveMessages(messages: List<ConversationMessage>) {
        TODO("Not yet implemented")
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun observeForMessages(
        conversationId: String,
        initialRefreshKey: Long
    ): Flow<PagingData<ConversationMessage>> {
        return Pager(
            config = PagingConfig(
                pageSize = 60,
                prefetchDistance = 5,
                initialLoadSize = 60
            ),
            initialKey = initialRefreshKey,
            remoteMediator = ConversationRemoteMediator(
                conversationId = conversationId,
                refreshTimestamp = initialRefreshKey,
                conversationLocalRepository = conversationLocalRepository,
                conversationApi = conversationApi,
                database = database
            ),
            pagingSourceFactory = {
                ConversationPagingSource(
                    conversationId = conversationId,
                    conversationLocalRepository = conversationLocalRepository,
                    coroutineScope = coroutineScope
                )
            }
        ).flow
    }

    override suspend fun obtainFirstUnreadMessage(conversationId: String): ConversationMessage? {
        TODO("Not yet implemented")
    }

    override suspend fun findMessageInConversation(
        conversationId: String,
        messageId: String
    ): ConversationMessage? {
        TODO("Not yet implemented")
    }

    override fun findIncompletedOutboxEntries(): Flow<List<MessageOutboxEntry>> {
        TODO("Not yet implemented")
    }

    override fun findIncompletedPendingMessageAcks(): Flow<List<PendingMessageAcks>> {
        TODO("Not yet implemented")
    }

    override suspend fun markOutboxEntryAsComplete(outboxId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun markPendingMessageAckAsComplete(id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun isPendingAckAvailable(
        conversationId: String,
        messageId: String,
        ack: MessageAcks
    ): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun savePendingMessageAck(pendingMessageAcks: PendingMessageAcks) {
        TODO("Not yet implemented")
    }

    override suspend fun obtainLastMessage(conversationId: String): ConversationMessage? {
        TODO("Not yet implemented")
    }

    override suspend fun obtainLastMessageSeqNo(conversationId: String): Long? {
        TODO("Not yet implemented")
    }

    override suspend fun syncHandshake(): Result<List<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun syncConversationFromServer(
        conversationId: String,
        lastSeqId: Long
    ): Result<List<ConversationMessage>> {
        TODO("Not yet implemented")
    }

    override suspend fun sendPendingMessageAck(acks: PendingMessageAcks): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun findBatchInMessageUploadsPending(batchId: String): List<MessageUpload> {
        TODO("Not yet implemented")
    }

    override fun isBatchCancelledInMessageOutbox(batchId: String): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun updateMessageUploadState(
        uploadId: String,
        updatedState: MessageUploadStatus
    ) {
        TODO("Not yet implemented")
    }
}