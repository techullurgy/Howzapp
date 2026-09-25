package com.techullurgy.howzapp.conversation.usecases

import com.techullurgy.howzapp.common.domain.ids.ConversationId
import com.techullurgy.howzapp.common.domain.ids.MessageId
import com.techullurgy.howzapp.common.domain.ids.UserId
import com.techullurgy.howzapp.conversation.db.repositories.MessageInboxActionRepository
import com.techullurgy.howzapp.conversation.domain.messages.MessageInboxActionType
import org.springframework.stereotype.Component

@Component
class AcknowledgeMessageReadUseCase(
    private val messageInboxActionRepository: MessageInboxActionRepository
) {
    suspend operator fun invoke(userId: UserId, conversationId: ConversationId, messageId: MessageId) {
        messageInboxActionRepository.find(
            conversationId = conversationId,
            messageId = messageId,
            userId = userId,
            actionType = MessageInboxActionType.READ_REQUIRED
        )?.let {
            messageInboxActionRepository.updateAsComplete(actionId = it.id)
        }

        // TODO: Update message status, if all are read
    }
}