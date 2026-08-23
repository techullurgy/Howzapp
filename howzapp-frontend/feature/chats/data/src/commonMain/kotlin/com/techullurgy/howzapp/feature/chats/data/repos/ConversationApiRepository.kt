package com.techullurgy.howzapp.feature.chats.data.repos

import com.techullurgy.howzapp.common.responses.MessageHistoryResponse

interface ConversationApiRepository {
    fun getMessagesAround(conversationId: String, loadKey: Long, loadSize: Int): MessageHistoryResponse
    fun getMessagesAfter(conversationId: String, loadKey: Long, loadSize: Int): MessageHistoryResponse
    fun getMessagesBefore(conversationId: String, loadKey: Long, loadSize: Int): MessageHistoryResponse
}