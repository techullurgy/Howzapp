package com.techullurgy.howzapp.feature.chats.domain.impl.tasks

import com.techullurgy.howzapp.feature.chats.domain.api.models.ConversationMutationData

internal class ReadReceiptSyncTask {
    suspend operator fun invoke(
        receipt: ConversationMutationData.ReadReceipt
    ) {

    }
}