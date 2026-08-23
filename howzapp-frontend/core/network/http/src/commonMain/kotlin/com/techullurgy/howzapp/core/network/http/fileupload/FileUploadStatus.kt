package com.techullurgy.howzapp.core.network.http.fileupload

sealed interface FileUploadStatus {
    data class Progress(val percentage: Double) : FileUploadStatus
    data object Success : FileUploadStatus
    data class Error(val exception: Throwable) : FileUploadStatus
    data object Cancelled : FileUploadStatus
}