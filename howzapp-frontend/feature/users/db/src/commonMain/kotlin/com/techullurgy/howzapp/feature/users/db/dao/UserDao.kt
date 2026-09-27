package com.techullurgy.howzapp.feature.users.db.dao

import androidx.room3.Dao
import androidx.room3.Upsert
import com.techullurgy.howzapp.feature.users.db.entities.UserEntity

@Dao
interface UserDao {
    @Upsert
    suspend fun upsertUser(user: UserEntity)
}