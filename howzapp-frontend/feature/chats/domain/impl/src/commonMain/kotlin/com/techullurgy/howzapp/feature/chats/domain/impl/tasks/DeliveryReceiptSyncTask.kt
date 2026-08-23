package com.techullurgy.howzapp.feature.chats.domain.impl.tasks

import com.techullurgy.howzapp.feature.chats.domain.api.models.ConversationMutationData

internal class DeliveryReceiptSyncTask {
    suspend operator fun invoke(
        receipt: ConversationMutationData.DeliveryReceipt
    ) {}
}