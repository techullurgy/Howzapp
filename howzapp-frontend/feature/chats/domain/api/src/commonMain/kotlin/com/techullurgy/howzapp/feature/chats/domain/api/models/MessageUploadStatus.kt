package com.techullurgy.howzapp.feature.chats.domain.api.models

sealed interface MessageUploadStatus {
    data object Initiated: MessageUploadStatus
    data class Uploading(val progress: Float): MessageUploadStatus
    data class Completed(val publicUrl: String): MessageUploadStatus
    data object Cancelled: MessageUploadStatus
    data class Failed(
        val error: Throwable
    ): MessageUploadStatus
}