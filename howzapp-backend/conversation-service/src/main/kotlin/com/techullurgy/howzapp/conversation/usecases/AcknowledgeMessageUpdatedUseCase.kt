package com.techullurgy.howzapp.conversation.usecases

import com.techullurgy.howzapp.common.domain.ids.ActionId
import com.techullurgy.howzapp.common.domain.ids.ConversationId
import com.techullurgy.howzapp.common.domain.ids.MessageId
import com.techullurgy.howzapp.common.domain.ids.UserId
import com.techullurgy.howzapp.conversation.db.repositories.MessageInboxActionRepository
import com.techullurgy.howzapp.conversation.domain.messages.MessageInboxActionType
import org.springframework.stereotype.Component

@Component
class AcknowledgeMessageUpdatedUseCase(
    private val messageInboxActionRepository: MessageInboxActionRepository
) {
    suspend operator fun invoke(userId: UserId, actionId: ActionId, conversationId: ConversationId, messageId: MessageId) {
        messageInboxActionRepository.find(
            actionId = actionId,
            conversationId = conversationId,
            messageId = messageId,
            userId = userId,
            actionType = MessageInboxActionType.MESSAGE_UPDATE
        )?.let {
            messageInboxActionRepository.updateAsComplete(actionId = it.id)
        }
    }
}