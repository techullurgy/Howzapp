package com.techullurgy.howzapp.common.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class CreateStatusRequest(
    @JsonProperty("mediaUrl") val mediaUrl: String?,
    @JsonProperty("caption") val caption: String?
)

data class StatusResponse(
    @JsonProperty("userId") val userId: String,
    @JsonProperty("statusId") val statusId: String,
    @JsonProperty("mediaUrl") val mediaUrl: String?,
    @JsonProperty("caption") val caption: String?,
    @JsonProperty("createdAt") val createdAt: Instant,
    @JsonProperty("expiresAt") val expiresAt: Instant
)