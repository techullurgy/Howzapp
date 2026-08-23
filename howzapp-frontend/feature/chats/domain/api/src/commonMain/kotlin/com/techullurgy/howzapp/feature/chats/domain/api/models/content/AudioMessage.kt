package com.techullurgy.howzapp.feature.chats.domain.api.models.content

data class AudioMessage(
    val media: Media,
    val title: String?,
    val artist: String?,
    val durationSeconds: Int
) : MessageContent