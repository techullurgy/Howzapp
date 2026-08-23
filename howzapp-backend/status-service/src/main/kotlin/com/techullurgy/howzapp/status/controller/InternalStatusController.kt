package com.techullurgy.howzapp.status.controller

import com.techullurgy.howzapp.common.dto.InternalStatusDto
import com.techullurgy.howzapp.status.service.StatusService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * INTERNAL API (For Sync-Service Inter-Service Communication)
 */
@RestController
@RequestMapping("/internal/v1/status")
class InternalStatusController(
    private val statusService: StatusService
) {

    @GetMapping("/feed")
    suspend fun getStatusFeedForUsers(
        @RequestParam userIds: List<String>,
        @RequestParam(defaultValue = "0") sinceTimestamp: Long
    ): ResponseEntity<List<InternalStatusDto>> {
        val feed = statusService.getActiveContactStatuses(userIds, sinceTimestamp)
        return ResponseEntity.ok(feed)
    }
}