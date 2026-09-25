package com.techullurgy.howzapp.feature.chats.domain.impl.tasks

import com.techullurgy.howzapp.feature.chats.domain.api.models.MessageAcks
import com.techullurgy.howzapp.feature.chats.domain.api.models.PendingMessageAcks
import com.techullurgy.howzapp.feature.chats.domain.api.repositories.ConversationRepository
import kotlin.time.Instant
import kotlin.uuid.Uuid

class MessageReceiptsRequiredHandlerTask(
    private val conversationRepository: ConversationRepository,
) {
    suspend operator fun invoke(
        ack: PendingMessageAcks,
        timestamp: Instant
    ) {
        val message = conversationRepository.findMessageInConversation(ack.conversationId, ack.messageId)

        if(message != null) {
            val isAlreadyAvailable = conversationRepository.isPendingAckAvailable(ack.conversationId, ack.messageId, ack.ack)
            if(!isAlreadyAvailable) {
                conversationRepository.savePendingMessageAck(
                    PendingMessageAcks(
                        id = Uuid.random().toString(),
                        conversationId = ack.conversationId,
                        messageId = ack.messageId,
                        ack = ack.ack
                    )
                )
            }
        } else {
            val lastMessage = conversationRepository.obtainLastMessage(ack.conversationId) ?: return

            // New Messages Ack, Ignore this ack. Handled by Sync Service
            if(timestamp > lastMessage.timestamp) return

            when(ack.ack) {
                MessageAcks.DeliveryReceipt -> {
                    // Not a New message Ack, Hence considered as Pending Delivery Ack
                    conversationRepository.savePendingMessageAck(
                        PendingMessageAcks(
                            id = Uuid.random().toString(),
                            conversationId = ack.conversationId,
                            messageId = ack.messageId,
                            ack = ack.ack
                        )
                    )
                }
                MessageAcks.ReadReceipt -> {
                    val firstUnreadMessage = conversationRepository.obtainFirstUnreadMessage(ack.conversationId)

                    if(firstUnreadMessage == null) {
                        // We don't have any unread messages here, Hence we consider, this as already read
                        conversationRepository.savePendingMessageAck(
                            PendingMessageAcks(
                                id = Uuid.random().toString(),
                                conversationId = ack.conversationId,
                                messageId = ack.messageId,
                                ack = ack.ack
                            )
                        )
                    } else if(timestamp < firstUnreadMessage.timestamp) {
                        conversationRepository.savePendingMessageAck(
                            PendingMessageAcks(
                                id = Uuid.random().toString(),
                                conversationId = ack.conversationId,
                                messageId = ack.messageId,
                                ack = ack.ack
                            )
                        )
                    }
                }
            }
        }
    }
}