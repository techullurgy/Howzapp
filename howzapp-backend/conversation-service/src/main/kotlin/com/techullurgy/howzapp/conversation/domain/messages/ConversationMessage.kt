package com.techullurgy.howzapp.conversation.domain.messages

import com.techullurgy.howzapp.common.domain.ids.ConversationId
import com.techullurgy.howzapp.common.domain.ids.MessageId
import com.techullurgy.howzapp.common.domain.ids.UserId
import kotlin.time.Instant


sealed interface ConversationMessage {
    val id: MessageId
    val conversationId: ConversationId
    val author: UserId
    val seqNo: Long
    val payload: String
    val status: MessageStatus
    val reactions: List<MessageReaction>
    val replyTo: MessageId?
    val timestamp: Instant
    val isDeleted: Boolean
}