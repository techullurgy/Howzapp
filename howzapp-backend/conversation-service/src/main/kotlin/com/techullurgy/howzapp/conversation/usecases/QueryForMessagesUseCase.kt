package com.techullurgy.howzapp.conversation.usecases

import com.techullurgy.howzapp.common.domain.ids.ConversationId
import com.techullurgy.howzapp.common.domain.ids.UserId
import com.techullurgy.howzapp.conversation.db.repositories.MessagesRepository
import com.techullurgy.howzapp.conversation.domain.messages.ConversationMessage
import org.springframework.stereotype.Component

@Component
class QueryForMessagesUseCase(
    private val messagesRepository: MessagesRepository
) {
    suspend operator fun invoke(
        user: UserId,
        conversationId: ConversationId,
        cursorSeqNo: Long,
        direction: Int,
        loadSize: Int // TODO: Pass 0 for Append (+ve) Queries
    ): List<ConversationMessage> {
        if(cursorSeqNo == -1L) {
            return queryFromLast(user, conversationId, loadSize)
        }

        return when(obtainDirection(direction)) {
            Direction.Prepend -> queryPrepend(user, conversationId, cursorSeqNo, loadSize)
            Direction.Append -> queryAppend(user, conversationId, cursorSeqNo, 0)
        }
    }

    private suspend fun queryPrepend(
        user: UserId,
        conversationId: ConversationId,
        cursorSeqNo: Long,
        loadSize: Int
    ): List<ConversationMessage> {
        return messagesRepository.queryPreviousMessages(
            user = user,
            conversation = conversationId,
            cursorSeqNo = cursorSeqNo,
            loadSize = loadSize
        )
    }

    private suspend fun queryAppend(
        user: UserId,
        conversationId: ConversationId,
        cursorSeqNo: Long,
        loadSize: Int
    ): List<ConversationMessage> {
        return messagesRepository.queryNextMessages(
            user = user,
            conversation = conversationId,
            cursorSeqNo = cursorSeqNo,
            loadSize = loadSize
        )
    }

    private suspend fun queryFromLast(
        user: UserId,
        conversationId: ConversationId,
        loadSize: Int
    ): List<ConversationMessage> {
        val lastSeqNo = messagesRepository.lastSeqNoFor(conversationId)

        return messagesRepository.queryPreviousMessages(
            user = user,
            conversation = conversationId,
            cursorSeqNo = lastSeqNo + 1, // exclusive
            loadSize = loadSize
        )
    }

    private fun obtainDirection(dir: Int): Direction {
        assert(dir != 0) { "Query Direction cannot be zero, (+ve)/(-ve)" }
        return when {
            dir < 0 -> Direction.Prepend
            else -> Direction.Append
        }
    }

    private enum class Direction { Prepend, Append }
}