package com.techullurgy.howzapp.feature.chats.db.relations

import androidx.room3.Embedded
import androidx.room3.Relation
import com.techullurgy.howzapp.feature.chats.db.entities.ConversationEntity
import com.techullurgy.howzapp.feature.chats.db.entities.DirectConversationEntity
import com.techullurgy.howzapp.feature.chats.db.entities.GroupConversationEntity

data class ConversationRelation(
    @Embedded val conversation: ConversationEntity?,

    @Relation(
        parentColumns = ["id"],
        entityColumns = ["conversationId"],
    )
    val direct: DirectConversationEntity?,

    @Relation(
        parentColumns = ["id"],
        entityColumns = ["conversationId"],
        entity = GroupConversationEntity::class
    )
    val group: GroupConversationRelation?
) {
    init {
        check(conversation != null)
        check(!(group == null && direct == null)) {
            "Both (Direct & Group) Conversation cannot be NULL in a ConversationRelation"
        }
    }
}