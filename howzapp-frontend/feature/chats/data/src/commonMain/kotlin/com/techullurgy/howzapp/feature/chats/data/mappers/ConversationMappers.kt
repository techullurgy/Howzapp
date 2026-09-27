package com.techullurgy.howzapp.feature.chats.data.mappers

import com.techullurgy.howzapp.feature.chats.db.models.GroupParticipantTypeStored
import com.techullurgy.howzapp.feature.chats.db.relations.ConversationRelation
import com.techullurgy.howzapp.feature.chats.domain.api.models.Conversation
import com.techullurgy.howzapp.feature.chats.domain.api.models.GroupParticipantType
import com.techullurgy.howzapp.feature.chats.domain.api.models.ParticipantInfo

fun ConversationRelation.toConversation(): Conversation {
    return when {
        direct != null -> {
            Conversation.Direct(
                conversationId = conversation!!.id,
                to = direct!!.to
            )
        }
        group != null -> {
            Conversation.Group(
                conversationId = conversation!!.id,
                title = group!!.group.title,
                avatarUrl = group!!.group.avatarUrl,
                createdAt = group!!.group.createdAt,
                participants = group!!.participants.map {
                    ParticipantInfo(
                        userId = it.userId,
                        joinedAt = it.joinedAt,
                        type = when(it.type) {
                            GroupParticipantTypeStored.ADMIN -> GroupParticipantType.ADMIN
                            GroupParticipantTypeStored.MEMBER -> GroupParticipantType.MEMBER
                        }
                    )
                }
            )
        }
        else -> TODO()
    }
}