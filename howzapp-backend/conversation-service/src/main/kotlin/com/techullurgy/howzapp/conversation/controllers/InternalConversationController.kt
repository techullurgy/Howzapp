package com.techullurgy.howzapp.conversation.controllers

import com.techullurgy.howzapp.common.dto.InternalAppendRequest
import com.techullurgy.howzapp.common.dto.InternalAppendResponse
import com.techullurgy.howzapp.common.dto.MessageResponse
import com.techullurgy.howzapp.conversation.db.entities.MessageEntity
import com.techullurgy.howzapp.conversation.db.repositories.ConversationRepository
import com.techullurgy.howzapp.conversation.service.CausalMutationService
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/internal/v1/conversations")
class InternalConversationController(
    private val repository: ConversationRepository,
    private val causalMutationService: CausalMutationService
) {

    @GetMapping("/{convId}/last-seq")
    suspend fun getServerLastSeqId(@PathVariable convId: String): ResponseEntity<Long> {
        val lastSeq = repository.getLastSeqId(convId)
        return ResponseEntity.ok(lastSeq)
    }

    @GetMapping("/{convId}/messages")
    suspend fun getMessagesSince(
        @PathVariable convId: String,
        @RequestParam(defaultValue = "0") sinceSeqId: Long,
        @RequestParam(defaultValue = "20") limit: Int
    ): ResponseEntity<List<MessageResponse>> {
        val messages = repository.findMessagesSinceSeqId(convId, sinceSeqId, limit)
            .map { it.toResponse() }
            .toList()
        return ResponseEntity.ok(messages)
    }

    @PostMapping("/mutations/execute")
    suspend fun executeCausalMutations(
        @RequestParam senderId: String,
        @RequestBody mutations: List<InternalAppendRequest>
    ): ResponseEntity<List<InternalAppendResponse>> {
        val results = causalMutationService.processOrderedMutations(senderId, mutations)
        return ResponseEntity.ok(results)
    }

    private fun MessageEntity.toResponse() = MessageResponse(
        conversationId = this.conversationId,
        seqId = this.seqId,
        messageId = this.messageId,
        senderId = this.senderId,
        contentType = this.contentType,
        body = this.body,
        status = this.status,
        createdAt = this.createdAt
    )
}