package com.techullurgy.howzapp.feature.chats.domain.api.models.content

data class ImageMessage(
    val media: Media,
    val caption: String?
): MessageContent