package com.techullurgy.howzapp.feature.chats.presentation.impl.models

import com.techullurgy.howzapp.feature.chats.domain.api.models.GroupParticipantType
import com.techullurgy.howzapp.feature.users.domain.api.models.User
import kotlin.time.Instant

data class ParticipantInfoUiItem(
    val user: User,
    val joinedAt: Instant,
    val type: GroupParticipantType
)