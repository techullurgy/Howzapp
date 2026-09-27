package com.techullurgy.howzapp.feature.chats.db.relations

import androidx.room3.Embedded
import androidx.room3.Relation
import com.techullurgy.howzapp.feature.chats.db.entities.GroupConversationEntity
import com.techullurgy.howzapp.feature.chats.db.entities.GroupConversationParticipantsCrossRef
import com.techullurgy.howzapp.feature.chats.db.projections.ParticipantInfoView

data class GroupConversationRelation(
    @Embedded val group: GroupConversationEntity,

    @Relation(
        parentColumns = ["conversationId"],
        entityColumns = ["conversationId"],
        projection = ["userId", "joinedAt", "type"],
        entity = GroupConversationParticipantsCrossRef::class,
    )
    val participants: List<ParticipantInfoView>
)