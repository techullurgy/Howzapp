package com.techullurgy.howzapp.feature.chats.domain.api.models.content

data class CallMessage(
    val callType: CallType,
    val durationSeconds: Int
) : MessageContent

enum class CallType {
    AUDIO,
    VIDEO,
    MISSED_AUDIO,
    MISSED_VIDEO
}