package com.techullurgy.howzapp.common.dto

import kotlinx.serialization.Serializable

@Serializable
data class MessagePendingSyncDto(
    val conversationId: String,
    val messageId: String,
    // For incoming message
    val isDeliveredSyncNeeded: Boolean,
    // for incoming message
    val isReadSyncNeeded: Boolean,
)