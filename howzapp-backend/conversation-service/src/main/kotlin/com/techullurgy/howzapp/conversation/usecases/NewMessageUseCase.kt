package com.techullurgy.howzapp.conversation.usecases

import com.techullurgy.howzapp.common.core.pubsub.PubSubEvent
import com.techullurgy.howzapp.common.core.pubsub.PubSubPublisher
import com.techullurgy.howzapp.common.core.pubsub.userOutboxChannel
import com.techullurgy.howzapp.common.domain.ids.ActionId
import com.techullurgy.howzapp.common.domain.ids.ConversationId
import com.techullurgy.howzapp.common.domain.ids.MessageId
import com.techullurgy.howzapp.common.domain.ids.UserId
import com.techullurgy.howzapp.conversation.db.repositories.ConversationRepository
import com.techullurgy.howzapp.conversation.db.repositories.MessageInboxActionRepository
import com.techullurgy.howzapp.conversation.db.repositories.MessagesRepository
import com.techullurgy.howzapp.conversation.domain.Conversation
import com.techullurgy.howzapp.conversation.domain.messages.ConversationMessage
import com.techullurgy.howzapp.conversation.domain.messages.MessageInboxAction
import com.techullurgy.howzapp.conversation.domain.messages.MessageInboxActionType
import com.techullurgy.howzapp.conversation.domain.messages.TextMessage
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.time.Clock

@Component
class NewMessageUseCase(
    private val conversationRepository: ConversationRepository,
    private val messagesRepository: MessagesRepository,
    private val messageInboxActionRepository: MessageInboxActionRepository,
    private val pubSubPublisher: PubSubPublisher<PubSubEvent>,
) {
    @Transactional
    suspend operator fun invoke(
        message: ConversationMessage
    ): ConversationMessage {
        val conversation = ensureConversationIsAvailableFor(message)

        val originalMessage = messagesRepository.create(
            convertToOriginalMessage(message)
        )

        markForIncomingMessageToRecipients(conversation, originalMessage)

        return originalMessage
    }

    private suspend fun markForIncomingMessageToRecipients(
        conversation: Conversation,
        message: ConversationMessage
    ) {
        val sender = message.author
        val recipients = conversation.participants
            .map { it.user }
            .filter { it != sender }

        // Save rcpt, read required,
        // increment unread counts

        recipients.forEach { user ->
            val rcvdRequired = MessageInboxAction(
                id = ActionId(UUID.randomUUID().toString()),
                conversationId = conversation.id,
                messageId = message.id,
                actionType = MessageInboxActionType.RCVD_REQUIRED,
                intendedTo = user
            )
            val readRequired = MessageInboxAction(
                id = ActionId(UUID.randomUUID().toString()),
                conversationId = conversation.id,
                messageId = message.id,
                actionType = MessageInboxActionType.READ_REQUIRED,
                intendedTo = user
            )

            messageInboxActionRepository.create(rcvdRequired)
            messageInboxActionRepository.create(readRequired)

            conversationRepository.incrementUnreceivedMessagesCountFor(conversation.id, user)
        }

        recipients.forEach { user ->
            pubSubPublisher.publish(
                channel = userOutboxChannel(user.id),
                message = PubSubEvent.SyncTrigger(message.conversationId)
            )
        }
    }

    private suspend fun convertToOriginalMessage(message: ConversationMessage): ConversationMessage {
        val lastSeqNo = messagesRepository.lastSeqNoFor(message.conversationId)

        return when(message) {
            is TextMessage -> {
                message.copy(
                    id = MessageId(UUID.randomUUID().toString()),
                    seqNo = lastSeqNo + 1, // TODO("Last Seq No Query")
                    timestamp = Clock.System.now()
                )
            }
        }
    }

    private suspend fun createNewDirectConversation(message: ConversationMessage): Conversation.Direct {
        ensureConversationIsDirect(message.conversationId)

        val participant1UserId = message.author.id
        val participant2UserId = message.conversationId.id
            .removePrefix(DIRECT_CONVERSATION_ID_PREFIX)
            .split(DIRECT_CONVERSATION_ID_SEPARATOR)
            .first { it != participant1UserId }

        // Create New Conversation
        val newConversation = Conversation.Direct(
            id = message.conversationId,
            participant1 = Conversation.Direct.Participant(UserId(participant1UserId)),
            participant2 = Conversation.Direct.Participant(UserId(participant2UserId))
        )

        conversationRepository.saveConversation(newConversation)

        return newConversation
    }

    private suspend fun ensureConversationIsAvailableFor(message: ConversationMessage): Conversation {
        return conversationRepository.findConversation(message.conversationId)
            ?: createNewDirectConversation(message)
    }

    private fun ensureConversationIsDirect(conversationId: ConversationId) {
        conversationId.id.startsWith(DIRECT_CONVERSATION_ID_PREFIX)
            .let { isDirect ->
                if(!isDirect) {
                    throw RuntimeException("Conversation ID $conversationId is not direct, Cannot create new conversation.")
                }
            }
    }
}

private const val DIRECT_CONVERSATION_ID_PREFIX = "DIRECT_#_"
private const val DIRECT_CONVERSATION_ID_SEPARATOR = "_#_"