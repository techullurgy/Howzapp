package com.techullurgy.howzapp.conversation.domain

import com.techullurgy.howzapp.common.domain.ids.ConversationId
import com.techullurgy.howzapp.common.domain.ids.UserId


sealed interface Conversation {
    val id: ConversationId
    val participants: List<ConversationParticipant>

    data class Direct(
        override val id: ConversationId,
        private val participant1: Participant,
        private val participant2: Participant,
    ): Conversation {
        override val participants: List<ConversationParticipant>
            get() = listOf(participant1, participant2)
                .map {
                    ConversationParticipant(
                        conversationId = id,
                        user = it.user,
                        role = ConversationParticipantRole.MEMBER,
                        joinedAt = null,
                        unreceivedMessagesCount = it.unreceivedMessagesCount
                    )
                }

        data class Participant(val user: UserId, val unreceivedMessagesCount: Int = 0)
    }

    data class Group(
        override val id: ConversationId,
        val name: String,
        val profilePictureUrl: String?,
        override val participants: List<ConversationParticipant>
    ): Conversation
}