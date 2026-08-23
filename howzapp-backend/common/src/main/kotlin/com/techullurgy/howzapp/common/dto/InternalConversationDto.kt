package com.techullurgy.howzapp.common.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class InternalMessageDto(
    val conversationId: String,
    val seqId: Long,
    val messageId: String,
    val senderId: String,
    val contentType: String,
    val body: String?,
    val status: String,
    val createdAt: Instant
)

data class InternalAppendRequest(
    @JsonProperty("senderId") val senderId: String,
    @JsonProperty("conversationId") val conversationId: String,
    @JsonProperty("clientMutationId") val clientMutationId: String,
    @JsonProperty("clientSeqId") val clientSeqId: Long,
    @JsonProperty("mutationType") val mutationType: String, // "SEND_MESSAGE", "READ_RECEIPT", "DELIVERY_RECEIPT"
    @JsonProperty("payload") val payload: String
)

data class InternalAppendResponse(
    @JsonProperty("mutationId") val mutationId: String,
    @JsonProperty("clientSeqId") val clientSeqId: Long,
    @JsonProperty("status") val status: String, // "SUCCESS", "FAILED", "BLOCKED_BY_PREVIOUS_FAILURE"
    @JsonProperty("serverSeqId") val serverSeqId: Long? = null,
    @JsonProperty("remappedConvId") val remappedConvId: String? = null, // Set if offline UUID collision was resolved
    @JsonProperty("errorMessage") val errorMessage: String? = null
)