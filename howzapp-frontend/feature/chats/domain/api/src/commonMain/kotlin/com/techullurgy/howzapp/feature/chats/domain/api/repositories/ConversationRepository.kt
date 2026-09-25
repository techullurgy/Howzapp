package com.techullurgy.howzapp.feature.chats.domain.api.repositories

import androidx.paging.PagingData
import com.techullurgy.howzapp.core.domain.UploadId
import com.techullurgy.howzapp.feature.chats.domain.api.models.ConversationMessage
import com.techullurgy.howzapp.feature.chats.domain.api.models.MessageAcks
import com.techullurgy.howzapp.feature.chats.domain.api.models.MessageOutboxEntry
import com.techullurgy.howzapp.feature.chats.domain.api.models.MessageOutboxStatus
import com.techullurgy.howzapp.feature.chats.domain.api.models.MessageUpload
import com.techullurgy.howzapp.feature.chats.domain.api.models.MessageUploadStatus
import com.techullurgy.howzapp.feature.chats.domain.api.models.PendingMessageAcks
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.MessageContent
import kotlinx.coroutines.flow.Flow

interface ConversationRepository {
    suspend fun saveMessages(messages: List<ConversationMessage>)

    fun observeForMessages(
        conversationId: String,
        initialRefreshKey: Long
    ): Flow<PagingData<ConversationMessage>>

    suspend fun obtainFirstUnreadMessage(conversationId: String): ConversationMessage?
    suspend fun findMessageInConversation(conversationId: String, messageId: String): ConversationMessage?

    fun findIncompletedOutboxEntries() : Flow<List<MessageOutboxEntry>>
    fun findIncompletedPendingMessageAcks(): Flow<List<PendingMessageAcks>>

    suspend fun markOutboxEntryAsComplete(outboxId: String)
    suspend fun markPendingMessageAckAsComplete(id: String)
    suspend fun isPendingAckAvailable(
        conversationId: String,
        messageId: String,
        ack: MessageAcks
    ): Boolean

    suspend fun savePendingMessageAck(pendingMessageAcks: PendingMessageAcks)
    suspend fun obtainLastMessage(conversationId: String): ConversationMessage?
    suspend fun obtainLastMessageSeqNo(conversationId: String): Long?
    /**
     * List of Conversation Ids to sync
     */
    suspend fun syncHandshake(): Result<List<String>>

    /**
     * List of Messages to sync from server
     */
    suspend fun syncConversationFromServer(conversationId: String, lastSeqId: Long): Result<List<ConversationMessage>>
    suspend fun sendPendingMessageAck(acks: PendingMessageAcks): Result<Unit>

    /**
     * List of uploads where status = null (pending) in a batch
     */
    suspend fun findBatchInMessageUploadsPending(batchId: String): List<MessageUpload>

    fun isBatchCancelledInMessageOutbox(batchId: String): Flow<Boolean>
    suspend fun updateMessageUploadState(
        uploadId: String,
        updatedState: MessageUploadStatus
    )

    suspend fun updateMessageOutboxStateTo(batchId: String, status: MessageOutboxStatus)

    /**
     * All Uploads in Batch == CompleteSuccess??
     */
    suspend fun isBatchUploadCompleteSuccessfully(batchId: String): Boolean
    suspend fun findMessageUploadEntry(batchId: String, uploadId: String): MessageUpload
    suspend fun upsertMessageOutboxEntry(entry: MessageOutboxEntry): MessageOutboxEntry
    suspend fun sendMessage(conversationId: String, batchId: String, payload: MessageContent): Result<ConversationMessage>
}