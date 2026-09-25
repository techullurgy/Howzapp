package com.techullurgy.howzapp.feature.chats.domain.api.models

data class MessageUpload(
    val batchId: String,
    val uploadId: String,
    val identifier: String,
    val status: MessageUploadStatus? = null
)