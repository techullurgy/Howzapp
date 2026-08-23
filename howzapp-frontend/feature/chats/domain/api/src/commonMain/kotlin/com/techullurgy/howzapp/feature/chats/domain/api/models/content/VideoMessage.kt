package com.techullurgy.howzapp.feature.chats.domain.api.models.content

data class VideoMessage(
    val media: Media,
    val caption: String?,
    val durationSeconds: Int
): MessageContent