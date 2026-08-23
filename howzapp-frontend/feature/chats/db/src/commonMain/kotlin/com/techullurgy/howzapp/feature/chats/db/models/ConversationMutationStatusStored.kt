package com.techullurgy.howzapp.feature.chats.db.models

enum class ConversationMutationStatusStored {
    // Eligible for cleanup
    Done,
    // Not yet touched for processing (initial / retryable state)
    Pending,
    // Currently Processing
    Processing,
    // Blocked for Processing
    Blocked
}