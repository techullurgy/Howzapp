@file:Suppress("unused")

package com.techullurgy.howzapp.feature.chats.data.tasks

import com.techullurgy.howzapp.core.database.Database
import com.techullurgy.howzapp.feature.chats.domain.api.models.Conversation
import com.techullurgy.howzapp.feature.chats.domain.api.models.ConversationMessage
import com.techullurgy.howzapp.feature.chats.domain.api.models.MessageAcks
import com.techullurgy.howzapp.feature.chats.domain.api.models.PendingMessageAcks
import com.techullurgy.howzapp.feature.chats.domain.api.repositories.ConversationRepository
import com.techullurgy.howzapp.feature.chats.domain.api.tasks.SaveMessagesToLocalDatabaseTask
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Singleton
import kotlin.uuid.Uuid

@Singleton
internal class DefaultSaveMessagesToLocalDatabaseTask(
    private val conversationRepository: ConversationRepository,
    @Provided private val database: Database
): SaveMessagesToLocalDatabaseTask {
    override suspend fun invoke(
        conversation: Conversation,
        messages: List<ConversationMessage>
    ) {
        database.withWriteTransaction {
            // TODO: Save Conversation to the database first, if its new


            // TODO: Need Pre-Processing for messages, before save ????
            conversationRepository.saveMessages(messages)

            messages.map {
                PendingMessageAcks(
                    id = Uuid.random().toString(),
                    conversationId = it.conversationId.id,
                    messageId = it.id.id,
                    ack = MessageAcks.DeliveryReceipt
                )
            }.forEach {
                conversationRepository.savePendingMessageAck(it)
            }
        }
    }
}