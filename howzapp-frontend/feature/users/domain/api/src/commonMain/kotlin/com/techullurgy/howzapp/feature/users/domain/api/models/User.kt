package com.techullurgy.howzapp.feature.users.domain.api.models

data class User(
    val userId: UserId,
    val profileUrl: String?,
    val userExistType: UserExistType,
    val contact: String,
    val name: String?,
    val onlineStatus: UserOnlineStatus
) {
    val displayName: String = name ?: contact
}
