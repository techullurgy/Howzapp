package com.techullurgy.howzapp.feature.chats.domain.api.models.content

data class StickerMessage(
    val media: Media,
    val animated: Boolean
) : MessageContent