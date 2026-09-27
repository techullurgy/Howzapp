package com.techullurgy.howzapp.feature.chats.presentation.impl.models

sealed interface OwnerMetadata {

    sealed interface Person: OwnerMetadata {
        val name: String
        val color: String
        val profileUrl: String?

        data class You(
            override val name: String,
            override val color: String,
            override val profileUrl: String?,
            val messageReceiverStatus: MessageReceiverStatus
        ): Person

        data class Other(
            override val name: String,
            override val color: String,
            override val profileUrl: String?,
            val messageReadStatus: MessageReadStatus,
            val isOnline: Boolean,
            val hasStatusUpdates: Boolean
        ): Person
    }

    data object System: OwnerMetadata

    enum class MessageReceiverStatus {
        SENT, RECEIVED, READ
    }

    enum class MessageReadStatus {
        UNREAD, READ
    }
}