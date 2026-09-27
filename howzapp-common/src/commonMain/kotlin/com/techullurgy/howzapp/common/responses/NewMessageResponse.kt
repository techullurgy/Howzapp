package com.techullurgy.howzapp.common.responses

import com.techullurgy.howzapp.common.dto.MessageDto
import kotlinx.serialization.Serializable

@Serializable
data class NewMessageResponse(
    val originalMessage: MessageDto,
    val localBatchId: String,
)
