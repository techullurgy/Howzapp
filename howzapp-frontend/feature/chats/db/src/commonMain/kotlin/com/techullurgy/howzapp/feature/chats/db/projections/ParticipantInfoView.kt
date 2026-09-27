package com.techullurgy.howzapp.feature.chats.db.projections

import com.techullurgy.howzapp.feature.chats.db.models.GroupParticipantTypeStored
import kotlin.time.Instant

data class ParticipantInfoView(
    val userId: String,
    val joinedAt: Instant,
    val type: GroupParticipantTypeStored
)
