package com.techullurgy.howzapp.conversation.service

import com.techullurgy.howzapp.common.domain.ids.ActionId
import com.techullurgy.howzapp.common.domain.ids.ConversationId
import com.techullurgy.howzapp.common.domain.ids.MessageId
import com.techullurgy.howzapp.common.domain.ids.UserId
import com.techullurgy.howzapp.conversation.db.repositories.MessageInboxActionRepository
import com.techullurgy.howzapp.conversation.db.repositories.MessagesRepository
import com.techullurgy.howzapp.conversation.domain.messages.ConversationMessage
import com.techullurgy.howzapp.conversation.domain.messages.MessageReaction
import com.techullurgy.howzapp.conversation.usecases.AcknowledgeMessageReadUseCase
import com.techullurgy.howzapp.conversation.usecases.AcknowledgeMessageReceivedUseCase
import com.techullurgy.howzapp.conversation.usecases.AcknowledgeMessageUpdatedUseCase
import com.techullurgy.howzapp.conversation.usecases.AddReactionForMessageUseCase
import com.techullurgy.howzapp.conversation.usecases.MarkMessageAsDeletedUseCase
import com.techullurgy.howzapp.conversation.usecases.NewMessageUseCase
import com.techullurgy.howzapp.conversation.usecases.QueryForMessagesUseCase
import com.techullurgy.howzapp.conversation.usecases.QueryMessageUseCase
import com.techullurgy.howzapp.conversation.usecases.RemoveReactionForMessageUseCase
import org.springframework.stereotype.Service

@Service
class MessagesService(
    private val newMessageUseCase: NewMessageUseCase,
    private val queryForMessagesUseCase: QueryForMessagesUseCase,
    private val queryMessageUseCase: QueryMessageUseCase,
    private val addReactionForMessageUseCase: AddReactionForMessageUseCase,
    private val removeReactionForMessageUseCase: RemoveReactionForMessageUseCase,
    private val markMessageAsDeletedUseCase: MarkMessageAsDeletedUseCase,
    private val acknowledgeMessageReadUseCase: AcknowledgeMessageReadUseCase,
    private val acknowledgeMessageReceivedUseCase: AcknowledgeMessageReceivedUseCase,
    private val acknowledgeMessageUpdatedUseCase: AcknowledgeMessageUpdatedUseCase,
) {
    /**
     * direction = -1 (previous), +1 (next)
     *      previous => size = loadSize
     *      next => size = 0 => Till the End => Will call only by sync request
     */
    suspend fun queryForMessages(user: UserId, conversation: ConversationId, cursorSeqNo: Long, direction: Int, loadSize: Int): List<ConversationMessage>
        = queryForMessagesUseCase(user, conversation, cursorSeqNo, direction, loadSize)


    suspend fun queryMessage(conversationId: ConversationId, messageId: MessageId): ConversationMessage?
        = queryMessageUseCase(conversationId, messageId)

    /**
     * 1) Take a temporary message and transform it into an Original Conversation Message
     * 2) Put Necessary Inbox actions for all the sender/recipients, unreadCounts
     * 3) Notify all recipients for this message (SyncEvent)
     */
    suspend fun newMessage(message: ConversationMessage): ConversationMessage
        = newMessageUseCase(message)

    suspend fun addReactionForMessage(conversationId: ConversationId, reaction: MessageReaction)
        = addReactionForMessageUseCase(conversationId, reaction)

    suspend fun removeReactionForMessage(conversationId: ConversationId, reaction: MessageReaction)
        = removeReactionForMessageUseCase(conversationId, reaction)

    suspend fun markMessageAsDeleted(conversationId: ConversationId, messageId: MessageId, requestedBy: UserId)
        = markMessageAsDeletedUseCase(conversationId, messageId, requestedBy)

    suspend fun messageIsReceivedTo(userId: UserId, conversationId: ConversationId, messageId: MessageId)
        = acknowledgeMessageReceivedUseCase(userId, conversationId, messageId)

    suspend fun messageIsReadBy(userId: UserId, conversationId: ConversationId, messageId: MessageId)
        = acknowledgeMessageReadUseCase(userId, conversationId, messageId)

    suspend fun messageIsUpdatedTo(userId: UserId, actionId: ActionId, conversationId: ConversationId, messageId: MessageId)
        = acknowledgeMessageUpdatedUseCase(userId, actionId, conversationId, messageId)
}