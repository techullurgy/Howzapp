package com.techullurgy.howzapp.feature.chats.domain.api.models.content

data class VoiceMessage(
    val media: Media,
    val durationSeconds: Int,
    val waveForm: List<Int>,
): MessageContent