package com.techullurgy.howzapp.core.network.fileupload


sealed interface UploadFileState {
    data object Initiated: UploadFileState
    data class Uploading(val progress: Float): UploadFileState
    data class Completed(val publicUrl: String): UploadFileState
    data object Cancelled: UploadFileState
    data class Failed(
        val error: Throwable
    ): UploadFileState
}
