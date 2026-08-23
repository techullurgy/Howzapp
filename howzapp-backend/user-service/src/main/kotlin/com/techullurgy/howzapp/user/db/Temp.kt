package com.techullurgy.howzapp.user.db

import org.springframework.data.annotation.Id
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Table("user_privacy")
data class UserPrivacyEntity(
    @Id val userId: String,
    val lastSeenPrivacy: String
)

@Table("user_contacts")
data class UserContactEntity(
    val userId: String,
    val contactUserId: String
)

@Repository
interface R2dbcUserPrivacyRepository : CoroutineCrudRepository<UserPrivacyEntity, String>

@Repository
interface R2dbcUserContactRepository : CoroutineCrudRepository<UserContactEntity, String> {

    @Query("SELECT contact_user_id FROM user_contacts WHERE user_id = :userId")
    fun findAllContactsByUserId(userId: String): kotlinx.coroutines.flow.Flow<String>

    @Query("SELECT COUNT(*) > 0 FROM user_contacts WHERE user_id = :userId AND contact_user_id = :contactUserId")
    suspend fun isContact(userId: String, contactUserId: String): Boolean
}