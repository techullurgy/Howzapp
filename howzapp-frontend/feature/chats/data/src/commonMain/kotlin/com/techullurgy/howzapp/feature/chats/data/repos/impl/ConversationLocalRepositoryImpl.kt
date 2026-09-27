@file:Suppress("unused")

package com.techullurgy.howzapp.feature.chats.data.repos.impl

import com.techullurgy.howzapp.core.database.Database
import com.techullurgy.howzapp.feature.chats.data.mappers.toConversation
import com.techullurgy.howzapp.feature.chats.data.mappers.toConversationMessage
import com.techullurgy.howzapp.feature.chats.data.mappers.toConversationMessageEntity
import com.techullurgy.howzapp.feature.chats.data.mappers.toOutboxMessage
import com.techullurgy.howzapp.feature.chats.data.repos.ConversationLocalRepository
import com.techullurgy.howzapp.feature.chats.db.dao.ConversationDao
import com.techullurgy.howzapp.feature.chats.db.dao.ConversationMessageDao
import com.techullurgy.howzapp.feature.chats.db.dao.ConversationMessageOutboxDao
import com.techullurgy.howzapp.feature.chats.db.entities.ConversationEntity
import com.techullurgy.howzapp.feature.chats.db.entities.DirectConversationEntity
import com.techullurgy.howzapp.feature.chats.db.entities.GroupConversationEntity
import com.techullurgy.howzapp.feature.chats.db.entities.GroupConversationParticipantsCrossRef
import com.techullurgy.howzapp.feature.chats.db.models.GroupParticipantTypeStored
import com.techullurgy.howzapp.feature.chats.domain.api.models.Conversation
import com.techullurgy.howzapp.feature.chats.domain.api.models.ConversationMessage
import com.techullurgy.howzapp.feature.chats.domain.api.models.GroupParticipantType
import com.techullurgy.howzapp.feature.chats.domain.api.models.OutboxMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Singleton

@Singleton
internal class ConversationLocalRepositoryImpl(
    @Provided private val conversationDao: ConversationDao,
    @Provided private val messageDao: ConversationMessageDao,
    @Provided private val outboxDao: ConversationMessageOutboxDao,
    @Provided private val database: Database,
): ConversationLocalRepository {
    override suspend fun saveConversation(conversation: Conversation) {
        database.withWriteTransaction {
            val conversationEntity = ConversationEntity(conversation.conversationId)
            conversationDao.upsertConversation(conversationEntity)
            when(conversation) {
                is Conversation.Direct -> {
                    val directConversationEntity = DirectConversationEntity(
                        conversationId = conversation.conversationId,
                        to = conversation.to
                    )
                    conversationDao.upsertDirectConversation(directConversationEntity)
                }
                is Conversation.Group -> {
                    val groupConversationEntity = GroupConversationEntity(
                        conversationId = conversation.conversationId,
                        title = conversation.title,
                        avatarUrl = conversation.avatarUrl,
                        createdAt = conversation.createdAt
                    )
                    conversationDao.upsertGroupConversation(groupConversationEntity)

                    conversation.participants.forEach { participant ->
                        val crossRef = GroupConversationParticipantsCrossRef(
                            conversationId = conversation.conversationId,
                            userId = participant.userId,
                            joinedAt = participant.joinedAt,
                            type = when(participant.type) {
                                GroupParticipantType.ADMIN -> GroupParticipantTypeStored.ADMIN
                                GroupParticipantType.MEMBER -> GroupParticipantTypeStored.MEMBER
                            }
                        )
                        conversationDao.upsertGroupParticipants(crossRef)
                    }
                }
            }
        }
    }

    override suspend fun saveMessage(message: ConversationMessage) {
        messageDao.upsert(message.toConversationMessageEntity())
    }

    override suspend fun saveMessages(messages: List<ConversationMessage>) {
        messageDao.upsertAll(messages.map { it.toConversationMessageEntity() })
    }

    override fun observeForConversation(conversationId: String): Flow<Conversation?> {
        return conversationDao.observeConversation(conversationId)
            .map {
                it?.toConversation()
            }
            .distinctUntilChanged()
    }

    override fun observeConversationMessages(conversationId: String): Flow<List<ConversationMessage>> {
        return messageDao.observeConversationMessages(conversationId)
            .map {
                it.map { message -> message.toConversationMessage() }
            }.distinctUntilChanged()
    }

    override fun observeForOutboxMessagesInComplete(conversationId: String): Flow<List<OutboxMessage>> {
        return outboxDao.observeOutboxMessagesInComplete(conversationId).map {
            it.map { out -> out.toOutboxMessage() }
        }.distinctUntilChanged()
    }

    override suspend fun getMessagesBefore(
        conversationId: String,
        currentTimestamp: Long,
        limit: Int
    ): List<ConversationMessage> {
        TODO("Not yet implemented")
    }

    override suspend fun getMessagesAfter(
        conversationId: String,
        currentTimestamp: Long,
        limit: Int
    ): List<ConversationMessage> {
        TODO("Not yet implemented")
    }

    override suspend fun getMessagesAround(
        conversationId: String,
        currentTimestamp: Long,
        limit: Int
    ): List<ConversationMessage> {
        TODO("Not yet implemented")
    }

    override suspend fun getFirstUnreadMessageTimestamp(conversationId: String): Long? {
        TODO("Not yet implemented")
    }

    override suspend fun getLatestTimestamp(conversationId: String): Long? {
        TODO("Not yet implemented")
    }

    override suspend fun hasMessages(conversationId: String): Boolean {
        TODO("Not yet implemented")
    }
}