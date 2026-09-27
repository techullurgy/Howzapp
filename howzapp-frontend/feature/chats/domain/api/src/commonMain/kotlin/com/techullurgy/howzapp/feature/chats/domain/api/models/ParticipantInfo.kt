package com.techullurgy.howzapp.feature.chats.domain.api.models

import kotlin.time.Instant

data class ParticipantInfo(
    val userId: String,
    val joinedAt: Instant,
    val type: GroupParticipantType
)
