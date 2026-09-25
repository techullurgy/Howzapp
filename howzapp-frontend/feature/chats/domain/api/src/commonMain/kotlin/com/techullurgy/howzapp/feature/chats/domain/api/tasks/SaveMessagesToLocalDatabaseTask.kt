package com.techullurgy.howzapp.feature.chats.domain.api.tasks

import com.techullurgy.howzapp.feature.chats.domain.api.models.Conversation
import com.techullurgy.howzapp.feature.chats.domain.api.models.ConversationMessage

interface SaveMessagesToLocalDatabaseTask {
    suspend operator fun invoke(
        conversation: Conversation,
        messages: List<ConversationMessage>
    )
}