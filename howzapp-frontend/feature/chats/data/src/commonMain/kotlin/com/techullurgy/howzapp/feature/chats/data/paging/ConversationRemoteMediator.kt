package com.techullurgy.howzapp.feature.chats.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.techullurgy.howzapp.core.database.Database
import com.techullurgy.howzapp.feature.chats.data.mappers.toConversationMessage
import com.techullurgy.howzapp.feature.chats.data.repos.ConversationApi
import com.techullurgy.howzapp.feature.chats.data.repos.ConversationLocalRepository
import com.techullurgy.howzapp.feature.chats.domain.api.models.ConversationMessage
import kotlin.time.Clock

@OptIn(ExperimentalPagingApi::class)
internal class ConversationRemoteMediator(
    private val conversationId: String,
    private val refreshTimestamp: Long?,
    private val conversationApi: ConversationApi,
    private val conversationLocalRepository: ConversationLocalRepository,
    private val database: Database
): RemoteMediator<Long, ConversationMessage>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Long, ConversationMessage>
    ): MediatorResult {
        return try {
            println("Mediator load LoadType[$loadType]")

            val loadKey = when(loadType) {
                LoadType.REFRESH -> {
                    // Use Provided initialTimestamp, or Closest Item timestamp
                    refreshTimestamp ?: state.anchorPosition?.let { position ->
                        state.closestItemToPosition(position)?.timestamp?.toEpochMilliseconds()
                    }
                }
                LoadType.PREPEND -> {
                    val firstItem = state.firstItemOrNull()
                    firstItem?.timestamp?.toEpochMilliseconds() ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    lastItem?.timestamp?.toEpochMilliseconds() ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }
            println("Mediator load LoadKey[$loadKey]")

            // Fetch network messages based on direction and loadKey
            val messageHistoryResponses = when (loadType) {
                LoadType.REFRESH -> {
                    val key = loadKey ?: -1
//                    conversationApi.getMessagesAround(conversationId, key, state.config.initialLoadSize)
                    TODO()
                }
                LoadType.PREPEND -> {
//                    conversationApi.getMessagesAfter(conversationId, loadKey!!, state.config.pageSize)
                    TODO()
                }
                LoadType.APPEND -> {
                    conversationApi.getMessagesBefore(conversationId, loadKey!!, state.config.pageSize)
                }
            }

            database.withWriteTransaction {
                messageHistoryResponses.history.forEach {
                    val message = it.toConversationMessage()
                    conversationLocalRepository.saveMessage(message)
                }
            }

            val endOfPaginationReached = messageHistoryResponses.history.firstOrNull { it.seqNo == 1L } != null
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch(e: Exception) {
            MediatorResult.Error(e)
        }
    }

    override suspend fun initialize(): InitializeAction {
        val hasMessages = conversationLocalRepository.hasMessages(conversationId)

        return if (hasMessages) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }
}