package com.techullurgy.howzapp.user.repos

import com.github.benmanes.caffeine.cache.Caffeine
import com.techullurgy.howzapp.common.core.pubsub.IPubSubManager
import com.techullurgy.howzapp.common.core.pubsub.PubSubConstants
import com.techullurgy.howzapp.user.db.R2dbcUserContactRepository
import com.techullurgy.howzapp.user.db.R2dbcUserPrivacyRepository
import com.techullurgy.howzapp.user.db.UserPrivacyEntity
import com.techullurgy.howzapp.user.models.PresencePrivacySetting
import kotlinx.coroutines.flow.toList
import java.time.Duration

class UserPresenceCachedRepository(
    private val pubSubManager: IPubSubManager,
    private val dbPrivacyRepository: R2dbcUserPrivacyRepository,
    private val dbContactRepository: R2dbcUserContactRepository
) {

    private val privacyL1Cache = Caffeine.newBuilder()
        .expireAfterWrite(Duration.ofMinutes(10))
        .maximumSize(100_000)
        .build<String, PresencePrivacySetting>()

    private val contactsL1Cache = Caffeine.newBuilder()
        .expireAfterWrite(Duration.ofMinutes(15))
        .maximumSize(100_000)
        .build<String, Set<String>>()

    private val stateL1Cache = Caffeine.newBuilder()
        .expireAfterWrite(Duration.ofSeconds(30))
        .maximumSize(500_000)
        .build<String, Pair<String, String?>>()

    suspend fun getPresencePrivacySetting(userId: String): PresencePrivacySetting {
        // 1. Check L1 Caffeine
        privacyL1Cache.getIfPresent(userId)?.let { return it }

        // 2. Check L2 Redis Hash
        val redisSettingStr = pubSubManager.hashGet(PubSubConstants.PRIVACY_KEY, userId)

        if (redisSettingStr != null) {
            val setting = parsePrivacySetting(redisSettingStr)
            privacyL1Cache.put(userId, setting)
            return setting
        }

        // 3. Fallback to Persistent DB (R2DBC)
        // val dbEntity = dbPrivacyRepository.findById(userId)
        // val finalSetting = dbEntity?.let { parsePrivacySetting(it.lastSeenPrivacy) } ?: PresencePrivacySetting.ALL
        val finalSetting = PresencePrivacySetting.ALL

        // Write-Back to L2 Redis & L1 Caffeine
        pubSubManager.hashPut(PubSubConstants.PRIVACY_KEY, userId, finalSetting.name)
        privacyL1Cache.put(userId, finalSetting)

        return finalSetting
    }

    suspend fun setPresencePrivacySetting(userId: String, setting: PresencePrivacySetting) {
        // Write-Through: 1. DB -> 2. Redis L2 -> 3. Local L1
        dbPrivacyRepository.save(UserPrivacyEntity(userId = userId, lastSeenPrivacy = setting.name))
        pubSubManager.hashPut(PubSubConstants.PRIVACY_KEY, userId, setting.name)
        privacyL1Cache.put(userId, setting)
    }

    // ==========================================
    // CONTACT GRAPH: L1 -> L2 -> R2DBC DB
    // ==========================================
    suspend fun getContacts(userId: String): Set<String> {
        // 1. Check L1
        contactsL1Cache.getIfPresent(userId)?.let { return it }

        // 2. Check L2 Redis Set
        val redisContacts = pubSubManager.setMembers("${PubSubConstants.CONTACTS_PREFIX}$userId")

        if (!redisContacts.isNullOrEmpty()) {
            val set = redisContacts.toSet()
            contactsL1Cache.put(userId, set)
            return set
        }

        // 3. Fallback to DB Query
        val dbContacts = dbContactRepository.findAllContactsByUserId(userId).toList().toSet()

        // Populate L2 Redis & L1 Caffeine if contacts exist
        if (dbContacts.isNotEmpty()) {
            pubSubManager.setAdd("${PubSubConstants.CONTACTS_PREFIX}$userId", dbContacts)
            contactsL1Cache.put(userId, dbContacts)
        }

        return dbContacts
    }

    suspend fun isContact(userId: String, targetUserId: String): Boolean {
        contactsL1Cache.getIfPresent(userId)?.let {
            return it.contains(targetUserId)
        }

        val isRedisMember = pubSubManager.setContains("${PubSubConstants.CONTACTS_PREFIX}$userId", targetUserId)

        if (isRedisMember == true) return true

        // Fallback to DB
        return dbContactRepository.isContact(userId, targetUserId)
    }

    // ==========================================
    // PRESENCE STATE: L1 -> L2 (Ephemeral data, no DB needed)
    // ==========================================
    suspend fun savePresenceState(userId: String, state: String, lastSeenStr: String?) {
        pubSubManager.valueSet("${PubSubConstants.PRESENCE_STATE_PREFIX}$userId", state)
        if (lastSeenStr != null) {
            pubSubManager.valueSet("${PubSubConstants.LAST_SEEN_PREFIX}$userId", lastSeenStr)
        }
        stateL1Cache.put(userId, Pair(state, lastSeenStr))
    }

    suspend fun getRawState(userId: String): Pair<String, String?> {
        stateL1Cache.getIfPresent(userId)?.let { return it }

        val state = pubSubManager.valueGet("${PubSubConstants.PRESENCE_STATE_PREFIX}$userId") ?: "OFFLINE"
        val lastSeen = pubSubManager.valueGet("${PubSubConstants.LAST_SEEN_PREFIX}$userId")

        val result = Pair(state, lastSeen)
        stateL1Cache.put(userId, result)
        return result
    }

    // --- Helpers ---
    fun invalidateUserPrivacy(userId: String) = privacyL1Cache.invalidate(userId)
    fun invalidateUserContacts(userId: String) = contactsL1Cache.invalidate(userId)

    private fun parsePrivacySetting(str: String): PresencePrivacySetting {
        return try { PresencePrivacySetting.valueOf(str) } catch (_: Exception) { PresencePrivacySetting.ALL }
    }
}