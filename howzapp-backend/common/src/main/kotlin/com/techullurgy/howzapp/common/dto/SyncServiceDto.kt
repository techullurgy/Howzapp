package com.techullurgy.howzapp.common.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class PushLocalChangesRequest(
    @JsonProperty("mutations") val mutations: List<LocalMutation>
)

data class LocalMutation(
    @JsonProperty("mutationId") val mutationId: String,
    @JsonProperty("convId") val convId: String,
    @JsonProperty("clientSeqId") val clientSeqId: Long,
    @JsonProperty("type") val type: String, // "SEND_MESSAGE", "READ_RECEIPT", "DELIVERY_RECEIPT"
    @JsonProperty("payload") val payload: String
)

data class PushLocalChangesResponse(
    @JsonProperty("results") val results: List<MutationResult>
)

data class MutationResult(
    @JsonProperty("mutationId") val mutationId: String,
    @JsonProperty("clientSeqId") val clientSeqId: Long,
    @JsonProperty("status") val status: String, // "SUCCESS", "FAILED", "BLOCKED_BY_PREVIOUS_FAILURE"
    @JsonProperty("serverSeqId") val serverSeqId: Long? = null,
    @JsonProperty("errorMessage") val errorMessage: String? = null
)

// --- Phase 2: Handshake ---
data class HandshakeRequest(
    @JsonProperty("conversations") val conversations: List<LocalConvHandshakeState>
)

data class LocalConvHandshakeState(
    @JsonProperty("convId") val convId: String,
    @JsonProperty("localLastSeqId") val localLastSeqId: Long
)

data class HandshakeResponse(
    @JsonProperty("conversationsMeta") val conversationsMeta: List<ConvSyncMeta>
)

data class ConvSyncMeta(
    @JsonProperty("convId") val convId: String,
    @JsonProperty("localLastSeqId") val localLastSeqId: Long,
    @JsonProperty("serverLastSeqId") val serverLastSeqId: Long,
    @JsonProperty("pendingCount") val pendingCount: Long,
    @JsonProperty("hasMore") val hasMore: Boolean
)

// --- Phase 3: Chunk Fetch ---
data class ChunkFetchRequest(
    @JsonProperty("convId") val convId: String,
    @JsonProperty("sinceSeqId") val sinceSeqId: Long,
    @JsonProperty("limit") val limit: Int = 20
)

data class ChunkFetchResponse(
    @JsonProperty("convId") val convId: String,
    @JsonProperty("messages") val messages: List<InternalMessageDto>,
    @JsonProperty("hasMore") val hasMore: Boolean,
    @JsonProperty("latestSeqId") val latestSeqId: Long
)

// --- Track 2: Status Sync ---
data class StatusSyncRequest(
    @JsonProperty("contactUserIds") val contactUserIds: List<String>,
    @JsonProperty("sinceTimestamp") val sinceTimestamp: Long = 0
)

data class StatusSyncResponse(
    @JsonProperty("statuses") val statuses: List<InternalStatusDto>
)