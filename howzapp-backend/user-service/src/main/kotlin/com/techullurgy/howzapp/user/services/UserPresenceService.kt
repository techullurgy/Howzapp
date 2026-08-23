package com.techullurgy.howzapp.user.services

import com.techullurgy.howzapp.common.core.pubsub.IPubSubManager
import com.techullurgy.howzapp.common.events.UserPresenceEvent
import com.techullurgy.howzapp.user.models.PresencePrivacySetting
import com.techullurgy.howzapp.user.repos.UserPresenceCachedRepository
import org.springframework.stereotype.Service
import tools.jackson.databind.ObjectMapper
import java.time.Instant

@Service
class UserPresenceService(
    private val repository: UserPresenceCachedRepository,
    private val pubSubManager: IPubSubManager,
    private val objectMapper: ObjectMapper
) {
    suspend fun queryPresenceForUser(requesterId: String, targetUserIds: List<String>): List<UserPresenceEvent> {
        return targetUserIds.map { targetId ->
            if (canViewPresence(requesterId, targetId)) {
                val (state, lastSeenStr) = repository.getRawState(targetId)
                UserPresenceEvent(
                    userId = targetId,
                    state = state,
                    lastSeen = lastSeenStr?.let { Instant.parse(it) }
                )
            } else {
                UserPresenceEvent(userId = targetId, state = "UNKNOWN", lastSeen = null)
            }
        }
    }

    suspend fun updatePresencePrivacySettings(
        userId: String,
        newSetting: PresencePrivacySetting
    ) {
        repository.setPresencePrivacySetting(userId, newSetting)

        // Broadcast L1 invalidation event across all Users-Service instances
        pubSubManager.convertAndSend("users:cache:invalidate", userId)

        val allContacts = repository.getContacts(userId)

        if (newSetting == PresencePrivacySetting.NOBODY) {
            val anonymizedPayload = objectMapper.writeValueAsString(
                UserPresenceEvent(userId = userId, state = "UNKNOWN", lastSeen = null)
            )
            allContacts.forEach { recipientId ->
                pubSubManager.convertAndSend("user:channel:$recipientId", anonymizedPayload)
            }
        } else {
            val (currentState, lastSeenStr) = repository.getRawState(userId)
            processPresenceStateChange(
                targetUserId = userId,
                state = currentState,
                lastSeen = lastSeenStr?.let { Instant.parse(it) }
            )
        }
    }

    suspend fun processPresenceStateChange(targetUserId: String, state: String, lastSeen: Instant? = null) {
        repository.savePresenceState(targetUserId, state, lastSeen?.toString())

        val privacy = repository.getPresencePrivacySetting(targetUserId)
        val recipients = resolveRecipients(targetUserId, privacy)

        val payload = objectMapper.writeValueAsString(
            UserPresenceEvent(userId = targetUserId, state = state, lastSeen = lastSeen)
        )

        recipients.forEach { recipientId ->
            pubSubManager.convertAndSend("subscriber:channel:$recipientId", payload)
        }
    }

    suspend fun updatePrivacySetting(userId: String, newSetting: PresencePrivacySetting) {
        repository.setPresencePrivacySetting(userId, newSetting)

        // Broadcast L1 invalidation event across all Users-Service instances
        pubSubManager.convertAndSend("users:cache:invalidate", userId)

        val allContacts = repository.getContacts(userId)

        if (newSetting == PresencePrivacySetting.NOBODY) {
            val anonymizedPayload = objectMapper.writeValueAsString(
                UserPresenceEvent(userId = userId, state = "UNKNOWN", lastSeen = null)
            )
            allContacts.forEach { recipientId ->
                pubSubManager.convertAndSend("subscriber:channel:$recipientId", anonymizedPayload)
            }
        } else {
            val (currentState, lastSeenStr) = repository.getRawState(userId)
            processPresenceStateChange(
                targetUserId = userId,
                state = currentState,
                lastSeen = lastSeenStr?.let { Instant.parse(it) }
            )
        }
    }

    private suspend fun canViewPresence(requesterId: String, targetId: String): Boolean {
        if (requesterId == targetId) return true
        return when (repository.getPresencePrivacySetting(targetId)) {
            PresencePrivacySetting.ALL -> true
            PresencePrivacySetting.NOBODY -> false
            PresencePrivacySetting.FRIENDS -> repository.isContact(targetId, requesterId)
        }
    }

    private suspend fun resolveRecipients(targetUserId: String, privacy: PresencePrivacySetting): Set<String> {
        return when (privacy) {
            PresencePrivacySetting.ALL, PresencePrivacySetting.FRIENDS -> repository.getContacts(targetUserId)
            PresencePrivacySetting.NOBODY -> emptySet()
        }
    }
}