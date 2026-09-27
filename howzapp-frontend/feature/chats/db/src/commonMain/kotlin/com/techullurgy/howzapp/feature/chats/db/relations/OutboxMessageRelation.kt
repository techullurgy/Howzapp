package com.techullurgy.howzapp.feature.chats.db.relations

import androidx.room3.Embedded
import androidx.room3.Relation
import com.techullurgy.howzapp.feature.chats.db.entities.ConversationMessageOutboxEntity
import com.techullurgy.howzapp.feature.chats.db.entities.MessageUploadsEntity

data class OutboxMessageRelation(
    @Embedded val outbox: ConversationMessageOutboxEntity,

    @Relation(
        parentColumns = ["id"],
        entityColumns = ["batchId"]
    )
    val upload: List<MessageUploadsEntity>
)
