package com.techullurgy.howzapp.feature.chats.domain.api.models

enum class ConversationMutationStatus {
    Done,
    Pending,
    Processing,
    Blocked
}

/*
PENDING   -> Ready to sync
SYNCING   -> Currently being processed
SYNCED    -> Done
FAILED    -> Retry later
CANCELLED -> User cancelled this mutation
PAUSED    -> Waiting because an earlier mutation was cancelled
*/