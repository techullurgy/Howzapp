package com.techullurgy.howzapp.sync.clients

import com.techullurgy.howzapp.common.dto.InternalAppendRequest
import com.techullurgy.howzapp.common.dto.InternalAppendResponse
import com.techullurgy.howzapp.common.dto.InternalMessageDto
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange
import org.springframework.web.service.annotation.PostExchange

@HttpExchange("/internal/v1/conversations")
interface ConversationServiceClient {
    @GetExchange("/{convId}/last-seq")
    suspend fun getServerLastSeqId(@PathVariable convId: String): Long

    @GetExchange("/{convId}/messages")
    suspend fun getMessagesSince(
        @PathVariable convId: String,
        @RequestParam sinceSeqId: Long,
        @RequestParam limit: Int
    ): List<InternalMessageDto>

    @PostExchange("/mutations/execute")
    suspend fun executeCausalMutations(
        @RequestParam senderId: String,
        @RequestBody mutations: List<InternalAppendRequest>
    ): List<InternalAppendResponse>
}