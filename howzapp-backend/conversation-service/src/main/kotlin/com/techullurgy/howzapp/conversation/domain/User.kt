package com.techullurgy.howzapp.conversation.domain

import com.techullurgy.howzapp.common.domain.ids.UserId

data class User(
    val id: UserId,
    val name: String,
    val profilePictureUrl: String?,
)
