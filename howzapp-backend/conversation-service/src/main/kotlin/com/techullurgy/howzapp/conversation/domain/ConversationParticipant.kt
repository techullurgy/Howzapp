package com.techullurgy.howzapp.conversation.domain

import com.techullurgy.howzapp.common.domain.ids.ConversationId
import com.techullurgy.howzapp.common.domain.ids.UserId
import kotlin.time.Instant

data class ConversationParticipant(
    val conversationId: ConversationId,
    val user: UserId,
    /**
     * The role of the participant in the conversation.
     * -> Direct conversations always have a role as MEMBER.
     * */
    val role: ConversationParticipantRole,
    /**
     * Tells us when the participant joined the conversation.
     * -> Direct conversations always have joinedAt as null.
     * */
    val joinedAt: Instant?,

    /**
     * Number of unreceived messages in this conversation, this participant has.
     * -> Updated, when a message is received by the participant.
     * */
    val unreceivedMessagesCount: Int = 0
)