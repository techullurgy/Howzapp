package com.techullurgy.howzapp.feature.users.db.entities

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.techullurgy.howzapp.feature.users.db.models.UserExistTypeStored
import kotlin.time.Instant

@Entity
data class UserEntity(
    @PrimaryKey val userId: String,
    val userExistType: UserExistTypeStored,
    val contact: String,
    val displayName: String?,
    val avatarUrl: String?,
    val lastSeenTime: Instant?
)