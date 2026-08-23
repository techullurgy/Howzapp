package com.techullurgy.howzapp.user.controllers

import com.techullurgy.howzapp.common.events.UserPresenceEvent
import com.techullurgy.howzapp.user.models.PresencePrivacySetting
import com.techullurgy.howzapp.user.services.UserPresenceService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
class UserPrivacySettingsController(
    private val userPresenceService: UserPresenceService
) {
    /**
     * Batch snapshot query for initial screen render
     */
    @PostMapping("/presence/query")
    suspend fun queryPresence(
        principal: Principal,
        @RequestBody request: PresenceQueryRequest
    ): ResponseEntity<PresenceQueryResponse> {
        val requesterId = principal.name
        val presences = userPresenceService.queryPresenceForUser(requesterId, request.targetUserIds)
        return ResponseEntity.ok(PresenceQueryResponse(presences))
    }

    /**
     * Updates user privacy settings
     */
    @PutMapping("/me/privacy")
    suspend fun updatePrivacy(
        principal: Principal,
        @RequestBody request: UpdatePrivacyRequest
    ): ResponseEntity<Map<String, String>> {
        val userId = principal.name
        userPresenceService.updatePrivacySetting(userId, request.lastSeenPrivacy)
        return ResponseEntity.ok(mapOf("status" to "SUCCESS"))
    }
}

data class PresenceQueryRequest(
    val targetUserIds: List<String>
)

data class PresenceQueryResponse(
    val presences: List<UserPresenceEvent>
)

data class UpdatePrivacyRequest(
    val lastSeenPrivacy: PresencePrivacySetting
)