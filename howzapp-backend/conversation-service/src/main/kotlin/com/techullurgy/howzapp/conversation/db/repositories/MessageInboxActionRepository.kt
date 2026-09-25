package com.techullurgy.howzapp.conversation.db.repositories

import com.techullurgy.howzapp.common.domain.ids.ActionId
import com.techullurgy.howzapp.common.domain.ids.ConversationId
import com.techullurgy.howzapp.common.domain.ids.MessageId
import com.techullurgy.howzapp.common.domain.ids.UserId
import com.techullurgy.howzapp.conversation.domain.messages.MessageInboxAction
import com.techullurgy.howzapp.conversation.domain.messages.MessageInboxActionType

interface MessageInboxActionRepository {
    suspend fun create(action: MessageInboxAction): MessageInboxAction?

    suspend fun find(actionId: ActionId): MessageInboxAction?
    suspend fun find(conversationId: ConversationId): List<MessageInboxAction>?
    suspend fun find(conversationId: ConversationId, messageId: MessageId): List<MessageInboxAction>?
    suspend fun find(conversationId: ConversationId, actionType: MessageInboxActionType): List<MessageInboxAction>?
    suspend fun find(conversationId: ConversationId, messageId: MessageId, actionType: MessageInboxActionType): List<MessageInboxAction>?
    suspend fun find(conversationId: ConversationId, userId: UserId, actionType: MessageInboxActionType): List<MessageInboxAction>?

    suspend fun find(conversationId: ConversationId, userId: UserId, messageId: MessageId, actionType: MessageInboxActionType): MessageInboxAction?
    suspend fun find(actionId: ActionId, conversationId: ConversationId, userId: UserId, messageId: MessageId, actionType: MessageInboxActionType): MessageInboxAction?

    suspend fun updateAsComplete(actionId: ActionId)

    suspend fun delete(actionId: ActionId)
    suspend fun delete(conversationId: ConversationId)
    suspend fun delete(conversationId: ConversationId, messageId: MessageId)
}