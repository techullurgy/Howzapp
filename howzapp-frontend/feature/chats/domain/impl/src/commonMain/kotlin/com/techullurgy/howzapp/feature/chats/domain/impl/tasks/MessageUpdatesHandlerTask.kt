package com.techullurgy.howzapp.feature.chats.domain.impl.tasks

import com.techullurgy.howzapp.feature.chats.domain.api.repositories.ConversationRepository

class MessageUpdatesHandlerTask(
    private val conversationRepository: ConversationRepository,
) {
    suspend operator fun invoke(conversationId: String, messageId: String) {
        val message = conversationRepository.findMessageInConversation(conversationId, messageId)

        if(message != null) {
            // => Message is available locally, Hence need update
            // Query for message from server
            // Save the message
        } else {
            // => Message is not available locally, Hence drop the update
            // Just Ack the message-update to server.
        }
    }
}