package com.techullurgy.howzapp.feature.chats.domain.api.models

sealed interface ConversationMutationData {
    data class NewMessage(
        val conversationId: String,
        val messageId: String,
    ): ConversationMutationData

    data class ReadReceipt(
        val conversationId: String,
        val messageId: String,
    ): ConversationMutationData

    data class DeliveryReceipt(
        val conversationId: String,
        val messageId: String
    ): ConversationMutationData
}