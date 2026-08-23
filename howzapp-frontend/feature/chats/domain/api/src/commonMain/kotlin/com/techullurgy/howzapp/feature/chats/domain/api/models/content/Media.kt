package com.techullurgy.howzapp.feature.chats.domain.api.models.content


data class Media(
    val id: MediaId,
    val url: String,
    val thumbnailUrl: String?,
    val mimeType: String,
    val sizeBytes: Long,
    val width: Int?,
    val height: Int?
)