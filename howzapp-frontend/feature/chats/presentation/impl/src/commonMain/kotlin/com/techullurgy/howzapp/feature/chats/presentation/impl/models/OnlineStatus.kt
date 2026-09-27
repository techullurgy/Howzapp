package com.techullurgy.howzapp.feature.chats.presentation.impl.models

import kotlin.time.Instant

sealed interface OnlineStatus {
    data object None: OnlineStatus
    data object Online: OnlineStatus
    data class LastSeen(val instant: Instant): OnlineStatus

    data object RecordingAudio: OnlineStatus
    data object Typing: OnlineStatus
}
