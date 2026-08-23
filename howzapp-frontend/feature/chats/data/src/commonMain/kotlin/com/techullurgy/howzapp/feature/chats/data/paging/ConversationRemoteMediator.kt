package com.techullurgy.howzapp.feature.chats.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.techullurgy.howzapp.feature.chats.data.handlers.MessageResponseHandler
import com.techullurgy.howzapp.feature.chats.data.repos.ConversationApiRepository
import com.techullurgy.howzapp.feature.chats.data.repos.ConversationLocalRepository
import com.techullurgy.howzapp.feature.chats.domain.api.models.ConversationMessage
import kotlin.time.Clock

@OptIn(ExperimentalPagingApi::class)
internal class ConversationRemoteMediator(
    private val conversationId: String,
    private val refreshTimestamp: Long?,
    private val apiService: ConversationApiRepository,
    private val messageResponseHandler: MessageResponseHandler,
    private val conversationLocalRepository: ConversationLocalRepository
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
                    val key = loadKey ?: Clock.System.now().toEpochMilliseconds()
                    apiService.getMessagesAround(conversationId, key, state.config.initialLoadSize)
                }
                LoadType.PREPEND -> {
                    apiService.getMessagesAfter(conversationId, loadKey!!, state.config.pageSize)
                }
                LoadType.APPEND -> {
                    apiService.getMessagesBefore(conversationId, loadKey!!, state.config.pageSize)
                }
            }

            messageResponseHandler.handle(messageHistoryResponses.messages)

            val endOfPaginationReached = messageHistoryResponses.messages.isEmpty()
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