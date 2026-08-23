package com.techullurgy.howzapp.core.session

data class AuthInfo(
    val id: String,
    val accessToken: String,
    val refreshToken: String
)