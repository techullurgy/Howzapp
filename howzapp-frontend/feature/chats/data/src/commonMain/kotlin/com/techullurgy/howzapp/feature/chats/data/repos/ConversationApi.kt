package com.techullurgy.howzapp.feature.chats.data.repos

import com.techullurgy.howzapp.common.responses.MessageHistoryResponse
import com.techullurgy.howzapp.feature.chats.domain.api.models.ConversationMessage
import com.techullurgy.howzapp.feature.chats.domain.api.models.PendingMessageAcks
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.MessageContent


interface ConversationApi {
    fun getMessagesBefore(conversationId: String, loadKey: Long, loadSize: Int): MessageHistoryResponse

    suspend fun syncHandshake(): Result<List<String>>

    suspend fun syncConversationFromServer(
        conversationId: String,
        lastSeqId: Long
    ): Result<List<ConversationMessage>>

    suspend fun sendPendingMessageAck(acks: PendingMessageAcks): Result<Unit>

    suspend fun sendMessage(
        conversationId: String,
        batchId: String,
        payload: MessageContent
    ): Result<ConversationMessage>
}