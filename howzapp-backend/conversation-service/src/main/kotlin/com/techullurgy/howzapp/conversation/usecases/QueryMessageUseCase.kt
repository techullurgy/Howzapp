package com.techullurgy.howzapp.conversation.usecases

import com.techullurgy.howzapp.common.domain.ids.ConversationId
import com.techullurgy.howzapp.common.domain.ids.MessageId
import com.techullurgy.howzapp.conversation.db.repositories.MessagesRepository
import com.techullurgy.howzapp.conversation.domain.messages.ConversationMessage
import org.springframework.stereotype.Component

@Component
class QueryMessageUseCase(
    private val messagesRepository: MessagesRepository
) {
    suspend operator fun invoke(conversationId: ConversationId, messageId: MessageId): ConversationMessage? {
        return messagesRepository.findBy(conversationId, messageId)
    }
}