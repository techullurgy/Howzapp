package com.techullurgy.howzapp.media.models

data class MediaMetadata(
    val uniqueKey: String,
    val originalUrl: String,
    val ownerUserId: String,
    val purpose: String,
    val isClaimed: Boolean
)