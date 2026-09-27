package com.techullurgy.howzapp.feature.chats.domain.api.models

import com.techullurgy.howzapp.core.domain.UploadId
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.MessageContent

data class OutboxMessageContent(
    val content: MessageContent,
    val uploadStatuses: List<Pair<UploadId, MessageUploadStatus>>? = null
)
