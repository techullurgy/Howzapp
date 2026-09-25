package com.techullurgy.howzapp.conversation.events.observers

import com.techullurgy.howzapp.common.domain.ids.UserId
import com.techullurgy.howzapp.conversation.events.ConversationEventSender

class UserConnectedEventObserver(
    private val conversationEventSender: ConversationEventSender
) {
    /**
     * This is called when a user connects to the server.
     * "user:presence:$userId" Topic Subscriber
     */
    suspend fun onConnected(userId: String) {
        val originalUserId = UserId(userId)

        // Check for any pending messages/actions for this user
        // If any, send them to the user via ConversationEventSender(SyncEvent)
    }
}