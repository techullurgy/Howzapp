package com.techullurgy.howzapp.feature.chats.domain.api.models.content

data class DocumentMessage(
    val media: MediaId,
    val fileName: String,
    val fileSize: Long,
    val mimeType: String
) : MessageContent