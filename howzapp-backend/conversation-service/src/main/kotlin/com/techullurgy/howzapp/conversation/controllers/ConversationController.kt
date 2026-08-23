package com.techullurgy.howzapp.conversation.controllers

import com.techullurgy.howzapp.common.dto.CreateConvResponse
import com.techullurgy.howzapp.common.dto.CreateDirectConvRequest
import com.techullurgy.howzapp.conversation.service.DirectConversationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/api/v1/conversations")
class PublicConversationController(
    private val directConversationService: DirectConversationService
) {

    @PostMapping("/direct")
    suspend fun getOrCreateDirect(
        principal: Principal,
        @RequestBody request: CreateDirectConvRequest
    ): ResponseEntity<CreateConvResponse> {
        val response = directConversationService.getOrCreateDirectConversation(principal.name, request.recipientId)
        return ResponseEntity.ok(response)
    }
}