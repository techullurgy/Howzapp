package com.techullurgy.howzapp.feature.chats.domain.api.tasks

interface PendingConversationMutationSyncTask {
    suspend operator fun invoke()
}