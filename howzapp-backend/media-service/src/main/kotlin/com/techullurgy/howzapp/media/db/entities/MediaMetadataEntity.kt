package com.techullurgy.howzapp.media.db.entities

import org.springframework.data.cassandra.core.mapping.Column
import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.Table
import java.time.Instant

@Table("media_metadata")
data class MediaMetadataEntity(
    @PrimaryKey("unique_key")
    val uniqueKey: String,

    @Column("original_url")
    val originalUrl: String,

    @Column("owner_user_id")
    val ownerUserId: String,

    @Column("media_type")
    val mediaType: String,

    @Column("purpose")
    val purpose: String,

    @Column("is_claimed")
    val isClaimed: Boolean = false,

    @Column("created_at")
    val createdAt: Instant = Instant.now()
)
