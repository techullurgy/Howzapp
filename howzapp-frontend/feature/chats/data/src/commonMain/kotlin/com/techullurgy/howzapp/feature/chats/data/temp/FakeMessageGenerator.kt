package com.techullurgy.howzapp.feature.chats.data.temp

import com.techullurgy.howzapp.feature.chats.data.repos.ConversationApiRepository
import com.techullurgy.howzapp.feature.chats.domain.api.models.ConversationId
import com.techullurgy.howzapp.feature.chats.domain.api.models.ConversationMessage
import com.techullurgy.howzapp.feature.chats.domain.api.models.ConversationMessageId
import com.techullurgy.howzapp.feature.chats.domain.api.models.MessageDeliveryStatus
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.TextMessage
import com.techullurgy.howzapp.feature.users.domain.api.models.UserId
import org.koin.core.annotation.Singleton
import kotlin.time.Clock
import kotlin.time.Instant

private const val INTERVAL_MS = 30 * 60 * 1000L // 30 minutes

@Singleton
class FakeMessageGenerator: ConversationApiRepository {
    private val latestTimestamp: Long = Clock.System.now().toEpochMilliseconds()

    override fun getMessagesAround(
        conversationId: String,
        loadKey: Long,
        loadSize: Int
    ): List<ConversationMessage> {

        val beforeSize = loadSize / 2
        val afterSize = loadSize - beforeSize

        val before = getMessagesBefore(conversationId, loadKey, beforeSize)
        val after = getMessagesAfter(conversationId, loadKey, afterSize)

        return (before + after)
            .sortedByDescending { it.timestamp }
    }

    override fun getMessagesAfter(
        conversationId: String,
        loadKey: Long,
        loadSize: Int
    ): List<ConversationMessage> {
        val end = latestTimestamp

        return generateSequence(loadKey + INTERVAL_MS) { it + INTERVAL_MS }
            .takeWhile { it <= end }
            .take(loadSize)
            .map { ts ->
                ConversationMessage(
                    id = ConversationMessageId("msg_$ts"),
                    timestamp = Instant.fromEpochMilliseconds(ts),
                    status = MessageDeliveryStatus.DELIVERED,
                    content = TextMessage("Message at $ts"),
                    senderId = UserId(""),
                    conversationId = ConversationId(conversationId)
                )
            }
            .toList()
            .sortedByDescending { it.timestamp }
    }

    override fun getMessagesBefore(
        conversationId: String,
        loadKey: Long,
        loadSize: Int
    ): List<ConversationMessage> {
        val start = minOf(loadKey, latestTimestamp)

        return (1..loadSize).map { index ->
            val ts = start - index * INTERVAL_MS

            ConversationMessage(
                id = ConversationMessageId("msg_$ts"),
                timestamp = Instant.fromEpochMilliseconds(ts),
                status = MessageDeliveryStatus.DELIVERED,
                content = TextMessage("Message at $ts"),
                senderId = UserId(""),
                conversationId = ConversationId(conversationId)
            )
        }
    }
}