package com.techullurgy.howzapp.common.events

import kotlinx.serialization.Serializable

@Serializable
data class UserConnectionEvent(
    val userId: UserId
)
