package com.techullurgy.howzapp.feature.chats.domain.api.tasks

import com.techullurgy.howzapp.feature.chats.domain.api.models.MessageOutboxEntry

interface MessageFileUploadTask {
    suspend operator fun invoke(entry: MessageOutboxEntry)
}