package com.techullurgy.howzapp.status.db.entities

import org.springframework.data.cassandra.core.cql.Ordering
import org.springframework.data.cassandra.core.cql.PrimaryKeyType
import org.springframework.data.cassandra.core.mapping.Column
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn
import org.springframework.data.cassandra.core.mapping.Table
import java.time.Instant

@Table("user_status_updates")
data class StatusEntity(
    @PrimaryKeyColumn(name = "user_id", type = PrimaryKeyType.PARTITIONED)
    val userId: String,

    @PrimaryKeyColumn(name = "created_at", type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    val createdAt: Instant = Instant.now(),

    @PrimaryKeyColumn(name = "status_id", type = PrimaryKeyType.CLUSTERED)
    val statusId: String,

    @Column("media_url") val mediaUrl: String?,
    @Column("caption") val caption: String?,
    @Column("expires_at") val expiresAt: Instant = Instant.now().plusSeconds(86400) // 24 Hours TTL
)