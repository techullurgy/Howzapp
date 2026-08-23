package com.techullurgy.howzapp.common.events

import java.time.Instant

data class UserPresenceEvent(
    val userId: String,
    val state: String,
    val lastSeen: Instant?
)
