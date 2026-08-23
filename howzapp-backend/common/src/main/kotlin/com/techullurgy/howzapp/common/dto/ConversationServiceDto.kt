package com.techullurgy.howzapp.common.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class MessageResponse(
    @JsonProperty("conversationId") val conversationId: String,
    @JsonProperty("seqId") val seqId: Long,
    @JsonProperty("messageId") val messageId: String,
    @JsonProperty("senderId") val senderId: String,
    @JsonProperty("contentType") val contentType: String,
    @JsonProperty("body") val body: String?,
    @JsonProperty("status") val status: String,
    @JsonProperty("createdAt") val createdAt: Instant
)

data class CreateDirectConvRequest(
    @JsonProperty("recipientId") val recipientId: String
)

data class CreateConvResponse(
    @JsonProperty("conversationId") val conversationId: String,
    @JsonProperty("isNew") val isNew: Boolean
)