package com.techullurgy.howzapp.media.dto

import com.fasterxml.jackson.annotation.JsonProperty

enum class MediaType { AUDIO, VIDEO, IMAGE, DOCUMENT }
enum class MediaPurpose { MEDIA_MESSAGE, STATUS_UPDATE, PROFILE_PICTURE }

data class PresignedUrlRequest(
    @JsonProperty("fileType") val fileType: MediaType,
    @JsonProperty("purpose") val purpose: MediaPurpose,
    @JsonProperty("fileSizeBytes") val fileSizeBytes: Long,
    @JsonProperty("mimeType") val mimeType: String
)

data class PresignedUrlResponse(
    @JsonProperty("uploadUrl") val uploadUrl: String,
    @JsonProperty("originalUrl") val originalUrl: String,
    @JsonProperty("uploadToken") val uploadToken: String,
    @JsonProperty("expiresIn") val expiresIn: Long = 900 // 15 minutes
)

data class ConfirmUploadRequest(
    @JsonProperty("uploadToken") val uploadToken: String,
    @JsonProperty("originalUrl") val originalUrl: String
)

data class ConfirmUploadResponse(
    @JsonProperty("uniqueKey") val uniqueKey: String,
    @JsonProperty("originalUrl") val originalUrl: String,
    @JsonProperty("purpose") val purpose: MediaPurpose,
    @JsonProperty("status") val status: String = "VERIFIED"
)

data class ValidateMediaRequest(
    @JsonProperty("uniqueKey") val uniqueKey: String,
    @JsonProperty("originalUrl") val originalUrl: String,
    @JsonProperty("purpose") val purpose: MediaPurpose
)

data class ValidateMediaResponse(
    @JsonProperty("isValid") val isValid: Boolean,
    @JsonProperty("ownerUserId") val ownerUserId: String? = null
)