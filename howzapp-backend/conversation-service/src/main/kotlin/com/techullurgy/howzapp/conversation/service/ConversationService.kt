package com.techullurgy.howzapp.conversation.service

import com.techullurgy.howzapp.common.domain.ids.ConversationId
import com.techullurgy.howzapp.common.domain.ids.UserId
import com.techullurgy.howzapp.conversation.db.repositories.ConversationRepository
import com.techullurgy.howzapp.conversation.domain.Conversation

class ConversationService(
    private val conversationRepository: ConversationRepository,
) {
    suspend fun newGroupConversation(conversation: Conversation.Group) { TODO() }

    /**
     * Once Added, publish a System Message to participants NewUserToGroup
     */
    suspend fun addParticipant(conversation: ConversationId, user: UserId): Boolean { TODO() }

    /**
     * Once Changed, publish a System Message to participants ProfilePictureChanged
     */
    suspend fun changeProfilePictureToGroupConversation(
        conversation: ConversationId,
        profilePictureUrl: String?,
    ): String { TODO() }

    /**
     * Once Leaved, publish a System Message to participants UserLeavedFromGroup
     */
    suspend fun leaveGroupConversation(): Boolean { TODO() }

    suspend fun syncHandshake(userId: UserId): List<ConversationId> { TODO() }

    suspend fun loadFull(userId: UserId): List<ConversationId> { TODO() }
}