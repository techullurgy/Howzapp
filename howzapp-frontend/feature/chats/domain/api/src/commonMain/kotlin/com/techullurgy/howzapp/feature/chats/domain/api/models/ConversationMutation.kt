package com.techullurgy.howzapp.feature.chats.domain.api.models

data class ConversationMutation(
    val id: String,
    val conversation: String,
    val createdAt: Long,
    val type: ConversationMutationType,
    val data: ConversationMutationData,
    val status: ConversationMutationStatus
) {
    init {
        require(
            when(type) {
                ConversationMutationType.NewMessage -> data is ConversationMutationData.NewMessage
                ConversationMutationType.DeliveryReceipt -> data is ConversationMutationData.DeliveryReceipt
                ConversationMutationType.ReadReceipt -> data is ConversationMutationData.ReadReceipt
            }
        )
    }
}
