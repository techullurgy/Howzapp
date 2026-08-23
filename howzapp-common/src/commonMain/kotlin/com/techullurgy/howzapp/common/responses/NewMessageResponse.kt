package com.techullurgy.howzapp.common.responses

import kotlinx.serialization.Serializable

@Serializable
data class NewMessageResponse(
    val messageId: String,
    // updated conversation id from server
    val conversationId: String,
    val localMessageId: String,
    // Has value, if update needed locally
    val localConversationId: String?,
)
