package com.techullurgy.howzapp.conversation.db.repositories

import com.techullurgy.howzapp.common.domain.ids.ConversationId
import com.techullurgy.howzapp.common.domain.ids.MessageId
import com.techullurgy.howzapp.common.domain.ids.UserId
import com.techullurgy.howzapp.conversation.domain.messages.ConversationMessage
import com.techullurgy.howzapp.conversation.domain.messages.MessageReaction
import com.techullurgy.howzapp.conversation.domain.messages.MessageStatus

interface MessagesRepository {
    suspend fun create(message: ConversationMessage): ConversationMessage

    suspend fun findBy(conversationId: ConversationId, messageId: MessageId): ConversationMessage?

    suspend fun updateStatus(messageId: MessageId, status: MessageStatus)

    suspend fun markAsDeleted(conversationId: ConversationId, messageId: MessageId): Boolean

    suspend fun addReaction(conversationId: ConversationId, reaction: MessageReaction)
    suspend fun removeReaction(conversationId: ConversationId, reaction: MessageReaction)

    suspend fun lastSeqNoFor(conversationId: ConversationId): Long

    /**
     * loadSize == 0, means -> Till the End of the conversation
     */
    suspend fun queryNextMessages(
        user: UserId,
        conversation: ConversationId,
        cursorSeqNo: Long,
        loadSize: Int
    ): List<ConversationMessage>

    suspend fun queryPreviousMessages(
        user: UserId,
        conversation: ConversationId,
        cursorSeqNo: Long,
        loadSize: Int
    ): List<ConversationMessage>
}