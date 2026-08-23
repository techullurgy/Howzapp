package com.techullurgy.howzapp.feature.users.domain.api.models

import kotlin.time.Instant

interface UserOnlineStatus {
    data object Online: UserOnlineStatus
    data object None: UserOnlineStatus

    data class LastSeen(
        val lastSeenAt: Instant
    ): UserOnlineStatus
}