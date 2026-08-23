package com.techullurgy.howzapp.user.services

import com.techullurgy.howzapp.common.core.pubsub.IPubSubManager
import com.techullurgy.howzapp.user.repos.UserPresenceCachedRepository
import kotlinx.coroutines.reactor.mono
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class UserPresencePrivacySettingInvalidationListener(
    private val pubSubManager: IPubSubManager,
    private val repository: UserPresenceCachedRepository
) {

    @EventListener(ApplicationReadyEvent::class)
    fun listenToCacheInvalidations() {
        val topic = "users:cache:invalidate"

        mono {
            pubSubManager.receive(listOf(topic))
                .collect { userId ->
                    // Invalidate L1 Caffeine Cache on this service node
                    repository.invalidateUserPrivacy(userId)
                    repository.invalidateUserContacts(userId)
                }
        }.subscribe()
    }
}