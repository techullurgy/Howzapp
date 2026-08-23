package com.techullurgy.howzapp.conversation.db.entities

import org.springframework.data.cassandra.core.cql.Ordering
import org.springframework.data.cassandra.core.cql.PrimaryKeyType
import org.springframework.data.cassandra.core.mapping.Column
import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn
import org.springframework.data.cassandra.core.mapping.Table
import java.time.Instant

@Table("messages")
data class MessageEntity(
    @PrimaryKeyColumn(name = "conversation_id", type = PrimaryKeyType.PARTITIONED)
    val conversationId: String,

    @PrimaryKeyColumn(name = "seq_id", type = PrimaryKeyType.CLUSTERED, ordering = Ordering.ASCENDING)
    val seqId: Long,

    @Column("message_id") val messageId: String,
    @Column("sender_id") val senderId: String,
    @Column("content_type") val contentType: String,
    @Column("body") val body: String?,
    @Column("status") val status: String,
    @Column("created_at") val createdAt: Instant = Instant.now()
)

@Table("user_conversations")
data class UserConversationEntity(
    @PrimaryKeyColumn(name = "user_id", type = PrimaryKeyType.PARTITIONED)
    val userId: String,

    @PrimaryKeyColumn(name = "conversation_id", type = PrimaryKeyType.CLUSTERED)
    val conversationId: String,

    @Column("is_direct") val isDirect: Boolean,
    @Column("last_seq_id") val lastSeqId: Long,
    @Column("unread_count") val unreadCount: Int,
    @Column("updated_at") val updatedAt: Instant = Instant.now()
)

@Table("conversation_participants")
data class ConversationParticipantEntity(
    @PrimaryKeyColumn(name = "conversation_id", type = PrimaryKeyType.PARTITIONED)
    val conversationId: String,

    @PrimaryKeyColumn(name = "user_id", type = PrimaryKeyType.CLUSTERED)
    val userId: String,

    @Column("role") val role: String = "MEMBER",
    @Column("joined_at") val joinedAt: Instant = Instant.now()
)

@Table("conversation_counters")
data class ConversationCounterEntity(
    @PrimaryKey("conversation_id") val conversationId: String,
    @Column("last_seq_id") val lastSeqId: Long
)