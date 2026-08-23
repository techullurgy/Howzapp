package com.techullurgy.howzapp.status.controller

import com.techullurgy.howzapp.common.dto.CreateStatusRequest
import com.techullurgy.howzapp.common.dto.StatusResponse
import com.techullurgy.howzapp.status.service.StatusService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

/**
 * PUBLIC API (For Mobile Clients)
 */
@RestController
@RequestMapping("/api/v1/status")
class PublicStatusController(
    private val statusService: StatusService
) {

    @PostMapping
    suspend fun createStatus(
        principal: Principal,
        @RequestBody request: CreateStatusRequest
    ): ResponseEntity<StatusResponse> {
        val response = statusService.postStatus(principal.name, request)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/user/{userId}")
    suspend fun getUserStatuses(
        @PathVariable userId: String
    ): ResponseEntity<List<StatusResponse>> {
        val responses = statusService.getUserStatuses(userId)
        return ResponseEntity.ok(responses)
    }
}