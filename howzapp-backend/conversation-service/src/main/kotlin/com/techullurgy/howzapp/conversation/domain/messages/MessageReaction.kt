package com.techullurgy.howzapp.conversation.domain.messages

import com.techullurgy.howzapp.common.domain.ids.MessageId
import com.techullurgy.howzapp.common.domain.ids.UserId


data class MessageReaction(
    val messageId: MessageId,
    val type: MessageReactionType,
    val by: UserId
)