package com.techullurgy.howzapp.conversation.domain.messages

import com.techullurgy.howzapp.common.domain.ids.ActionId
import com.techullurgy.howzapp.common.domain.ids.ConversationId
import com.techullurgy.howzapp.common.domain.ids.MessageId
import com.techullurgy.howzapp.common.domain.ids.UserId

data class MessageInboxAction(
    val id: ActionId,
    val conversationId: ConversationId,
    val messageId: MessageId,
    val actionType: MessageInboxActionType,
    val intendedTo: UserId
)