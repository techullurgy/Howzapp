package com.techullurgy.howzapp.feature.chats.domain.api.models.content

data class ImageMessage(
    val medias: List<Media>,
    val caption: String?
): MessageContent