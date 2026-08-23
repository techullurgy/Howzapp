package com.techullurgy.howzapp.sync.clients

import com.techullurgy.howzapp.common.dto.InternalStatusDto
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange

@HttpExchange("/internal/v1/status")
interface StatusServiceClient {

    @GetExchange("/feed")
    suspend fun getStatusFeedForUsers(
        @RequestParam userIds: List<String>,
        @RequestParam sinceTimestamp: Long
    ): List<InternalStatusDto>
}