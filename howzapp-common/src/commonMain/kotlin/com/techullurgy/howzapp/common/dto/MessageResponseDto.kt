package com.techullurgy.howzapp.common.dto

import kotlinx.serialization.Serializable

@Serializable
data class MessageResponseDto(
    val message: MessageDto,
    val pendingSync: MessagePendingSyncDto?,
    // Contains LocalMessageId, if it needs update
    val localMessageIdSync: String? = null
)

@Serializable
data class MessagePendingSyncDto(
    // For incoming message
    val isDeliveredSyncNeeded: Boolean,
    // for incoming message
    val isReadSyncNeeded: Boolean,
)