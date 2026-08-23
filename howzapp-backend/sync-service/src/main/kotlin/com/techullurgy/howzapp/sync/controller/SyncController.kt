package com.techullurgy.howzapp.sync.controller

import com.techullurgy.howzapp.common.dto.ChunkFetchRequest
import com.techullurgy.howzapp.common.dto.ChunkFetchResponse
import com.techullurgy.howzapp.common.dto.HandshakeRequest
import com.techullurgy.howzapp.common.dto.HandshakeResponse
import com.techullurgy.howzapp.common.dto.PushLocalChangesRequest
import com.techullurgy.howzapp.common.dto.PushLocalChangesResponse
import com.techullurgy.howzapp.common.dto.StatusSyncRequest
import com.techullurgy.howzapp.common.dto.StatusSyncResponse
import com.techullurgy.howzapp.sync.service.SyncService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/api/v1/sync")
class SyncController(
    private val syncService: SyncService
) {

    /**
     * Track 1 - Phase 1: Push offline queued local mutations
     */
    @PostMapping("/push-local")
    suspend fun pushLocalChanges(
        principal: Principal,
        @RequestBody request: PushLocalChangesRequest
    ): ResponseEntity<PushLocalChangesResponse> {
        val response = syncService.pushLocalChanges(principal.name, request)
        return ResponseEntity.ok(response)
    }

    /**
     * Track 1 - Phase 2: Metadata gap handshake
     */
    @PostMapping("/handshake")
    suspend fun handshake(
        @RequestBody request: HandshakeRequest
    ): ResponseEntity<HandshakeResponse> {
        val response = syncService.processHandshake(request)
        return ResponseEntity.ok(response)
    }

    /**
     * Track 1 - Phase 3: Paginated chunk pull
     */
    @PostMapping("/chunk")
    suspend fun fetchChunk(
        @RequestBody request: ChunkFetchRequest
    ): ResponseEntity<ChunkFetchResponse> {
        val response = syncService.fetchMessageChunk(request)
        return ResponseEntity.ok(response)
    }

    /**
     * Track 2: Status sync (Runs asynchronously in background)
     */
    @PostMapping("/status")
    suspend fun syncStatusFeed(
        @RequestBody request: StatusSyncRequest
    ): ResponseEntity<StatusSyncResponse> {
        val response = syncService.syncStatuses(request)
        return ResponseEntity.ok(response)
    }
}