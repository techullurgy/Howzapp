package com.techullurgy.howzapp.feature.chats.domain.api.models.content

data class LocationMessage(
    val latitude: Double,
    val longitude: Double,
    val address: String?,
    val liveDurationSeconds: Int?
) : MessageContent