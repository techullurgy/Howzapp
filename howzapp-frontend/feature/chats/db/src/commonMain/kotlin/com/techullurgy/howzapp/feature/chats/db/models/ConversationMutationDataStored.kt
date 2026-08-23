package com.techullurgy.howzapp.feature.chats.db.models

sealed interface ConversationMutationDataStored {
    data class NewMessage(
        val conversationId: String,
        val messageId: String,
    ): ConversationMutationDataStored

    data class ReadReceipt(
        val conversationId: String,
        val messageId: String,
    ): ConversationMutationDataStored

    data class DeliveryReceipt(
        val conversationId: String,
        val messageId: String
    ): ConversationMutationDataStored
}