package com.techullurgy.howzapp.sync.service

import com.techullurgy.howzapp.common.dto.ChunkFetchRequest
import com.techullurgy.howzapp.common.dto.ChunkFetchResponse
import com.techullurgy.howzapp.common.dto.ConvSyncMeta
import com.techullurgy.howzapp.common.dto.HandshakeRequest
import com.techullurgy.howzapp.common.dto.HandshakeResponse
import com.techullurgy.howzapp.common.dto.InternalAppendRequest
import com.techullurgy.howzapp.common.dto.MutationResult
import com.techullurgy.howzapp.common.dto.PushLocalChangesRequest
import com.techullurgy.howzapp.common.dto.PushLocalChangesResponse
import com.techullurgy.howzapp.common.dto.StatusSyncRequest
import com.techullurgy.howzapp.common.dto.StatusSyncResponse
import com.techullurgy.howzapp.sync.clients.ConversationServiceClient
import com.techullurgy.howzapp.sync.clients.StatusServiceClient
import org.springframework.stereotype.Service

@Service
class SyncService(
    private val conversationClient: ConversationServiceClient,
    private val statusClient: StatusServiceClient
) {

    /**
     * Track 1 - Phase 1: Flushes local offline mutations in strict FIFO order
     */
    suspend fun pushLocalChanges(senderId: String, request: PushLocalChangesRequest): PushLocalChangesResponse {
        val internalRequests = request.mutations.map {
            InternalAppendRequest(
                senderId = senderId,
                conversationId = it.convId,
                clientMutationId = it.mutationId,
                clientSeqId = it.clientSeqId,
                mutationType = it.type,
                payload = it.payload
            )
        }

        val internalResults = conversationClient.executeCausalMutations(senderId, internalRequests)

        val results = internalResults.map {
            MutationResult(
                mutationId = it.mutationId,
                clientSeqId = it.clientSeqId,
                status = it.status,
                serverSeqId = it.serverSeqId,
                errorMessage = it.errorMessage
            )
        }

        return PushLocalChangesResponse(results = results)
    }

    /**
     * Track 1 - Phase 2: Handshake query for gap calculation
     */
    suspend fun processHandshake(request: HandshakeRequest): HandshakeResponse {
        val metaList = request.conversations.map { localConv ->
            val serverLastSeq = conversationClient.getServerLastSeqId(localConv.convId)
            val pendingCount = (serverLastSeq - localConv.localLastSeqId).coerceAtLeast(0)

            ConvSyncMeta(
                convId = localConv.convId,
                localLastSeqId = localConv.localLastSeqId,
                serverLastSeqId = serverLastSeq,
                pendingCount = pendingCount,
                hasMore = pendingCount > 0
            )
        }

        return HandshakeResponse(conversationsMeta = metaList)
    }

    /**
     * Track 1 - Phase 3: Paginated range query pull
     */
    suspend fun fetchMessageChunk(request: ChunkFetchRequest): ChunkFetchResponse {
        val limit = request.limit.coerceIn(1, 50)

        // Request limit + 1 from Conversation Service to evaluate `hasMore` without additional DB queries
        val fetched = conversationClient.getMessagesSince(
            convId = request.convId,
            sinceSeqId = request.sinceSeqId,
            limit = limit + 1
        )

        val hasMore = fetched.size > limit
        val chunk = if (hasMore) fetched.take(limit) else fetched
        val latestSeqId = chunk.lastOrNull()?.seqId ?: request.sinceSeqId

        return ChunkFetchResponse(
            convId = request.convId,
            messages = chunk,
            hasMore = hasMore,
            latestSeqId = latestSeqId
        )
    }

    /**
     * Track 2: Asynchronous Status/Stories sync
     */
    suspend fun syncStatuses(request: StatusSyncRequest): StatusSyncResponse {
        if (request.contactUserIds.isEmpty()) {
            return StatusSyncResponse(statuses = emptyList())
        }

        val statuses = statusClient.getStatusFeedForUsers(
            userIds = request.contactUserIds,
            sinceTimestamp = request.sinceTimestamp
        )

        return StatusSyncResponse(statuses = statuses)
    }
}