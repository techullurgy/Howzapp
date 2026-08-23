package com.techullurgy.howzapp.feature.users.domain.api.models

data class User(
    val userId: UserId,
    val profileUrl: String?,
    val onlineStatus: UserOnlineStatus
)
