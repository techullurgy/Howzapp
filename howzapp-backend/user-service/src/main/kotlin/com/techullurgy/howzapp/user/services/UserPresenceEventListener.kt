package com.techullurgy.howzapp.user.services

import com.techullurgy.howzapp.common.core.pubsub.IPubSubManager
import com.techullurgy.howzapp.common.core.pubsub.PubSubConstants
import com.techullurgy.howzapp.common.events.UserPresenceEvent
import kotlinx.coroutines.reactor.mono
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import tools.jackson.databind.ObjectMapper
import tools.jackson.module.kotlin.readValue

@Component
class UserPresenceEventListener(
    private val pubSubManager: IPubSubManager,
    private val userPresenceService: UserPresenceService,
    private val objectMapper: ObjectMapper
) {
    @EventListener(ApplicationReadyEvent::class)
    fun listenToSystemPresenceEvents() {
        // Internal channel where edge servers publish raw connect/disconnect events
        val topic = PubSubConstants.USER_PRESENCE_EVENTS_CHANNEL

        mono {
            pubSubManager.receive(listOf(topic))
                .collect { message ->
                    try {
                        val event = objectMapper.readValue<UserPresenceEvent>(message)
                        userPresenceService.processPresenceStateChange(
                            targetUserId = event.userId,
                            state = event.state,
                            lastSeen = event.lastSeen
                        )
                    } catch (_: Exception) {}
                }
        }.subscribe()
    }
}