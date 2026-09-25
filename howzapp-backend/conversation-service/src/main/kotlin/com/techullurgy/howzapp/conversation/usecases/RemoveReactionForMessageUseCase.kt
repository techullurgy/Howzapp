package com.techullurgy.howzapp.conversation.usecases

import com.techullurgy.howzapp.common.domain.ids.ActionId
import com.techullurgy.howzapp.common.domain.ids.ConversationId
import com.techullurgy.howzapp.conversation.db.repositories.ConversationRepository
import com.techullurgy.howzapp.conversation.db.repositories.MessageInboxActionRepository
import com.techullurgy.howzapp.conversation.db.repositories.MessagesRepository
import com.techullurgy.howzapp.conversation.domain.messages.MessageInboxAction
import com.techullurgy.howzapp.conversation.domain.messages.MessageInboxActionType
import com.techullurgy.howzapp.conversation.domain.messages.MessageReaction
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class RemoveReactionForMessageUseCase(
    private val conversationRepository: ConversationRepository,
    private val messagesRepository: MessagesRepository,
    private val messageInboxActionRepository: MessageInboxActionRepository
) {
    suspend operator fun invoke(
        conversationId: ConversationId,
        reaction: MessageReaction
    ) {
        messagesRepository.removeReaction(conversationId, reaction)

        val recipients = conversationRepository.findConversation(conversationId)!!.participants

        recipients.forEach {
            messageInboxActionRepository.create(
                MessageInboxAction(
                    id = ActionId(UUID.randomUUID().toString()),
                    conversationId = conversationId,
                    messageId = reaction.messageId,
                    actionType = MessageInboxActionType.MESSAGE_UPDATE,
                    intendedTo = it.user
                )
            )
        }
    }
}