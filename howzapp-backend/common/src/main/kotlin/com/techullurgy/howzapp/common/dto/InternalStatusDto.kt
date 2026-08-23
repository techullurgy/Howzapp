package com.techullurgy.howzapp.common.dto

import java.time.Instant

// --- Internal Status DTOs ---
data class InternalStatusDto(
    val userId: String,
    val statusId: String,
    val mediaUrl: String?,
    val caption: String?,
    val createdAt: Instant,
    val expiresAt: Instant
)