package com.techullurgy.howzapp.conversation.domain.messages

import com.techullurgy.howzapp.common.domain.ids.ConversationId
import com.techullurgy.howzapp.common.domain.ids.MessageId
import com.techullurgy.howzapp.common.domain.ids.UserId
import kotlin.time.Instant

data class TextMessage(
    val text: String,

    override val id: MessageId,
    override val conversationId: ConversationId,
    override val author: UserId,
    override val seqNo: Long,
    override val status: MessageStatus,
    override val reactions: List<MessageReaction>,
    override val replyTo: MessageId?,
    override val timestamp: Instant,
    override val isDeleted: Boolean,
): ConversationMessage {
    override val payload: String = text
}