package com.techullurgy.howzapp.conversation.events

interface ConversationEventSender {
    /**
     * Send them to the user via the outbox topic.
     * Publish on "user:outbox:$userId" Topic
     */
    fun send(event: ConversationEvent): Boolean
}