package com.techullurgy.howzapp.core.network.fileupload

data class UploadFileUpdate(
    val uploadId: UploadId,
    val state: UploadFileState
)