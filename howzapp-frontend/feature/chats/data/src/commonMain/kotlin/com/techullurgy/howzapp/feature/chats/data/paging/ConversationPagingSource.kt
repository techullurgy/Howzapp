package com.techullurgy.howzapp.feature.chats.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.techullurgy.howzapp.feature.chats.data.repos.ConversationLocalRepository
import com.techullurgy.howzapp.feature.chats.domain.api.models.ConversationMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.time.Clock

internal class ConversationPagingSource(
    private val conversationId: String,
    private val conversationLocalRepository: ConversationLocalRepository,
    coroutineScope: CoroutineScope
): PagingSource<Long, ConversationMessage>() {

    init {
        conversationLocalRepository.observeConversationMessages(conversationId)
            .drop(1) // Skip initial invalidation
            .onEach { invalidate() }
            .launchIn(coroutineScope)
    }

    override fun getRefreshKey(state: PagingState<Long, ConversationMessage>): Long? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.timestamp?.toEpochMilliseconds()
        }
    }

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, ConversationMessage> {
        return try {
            val currentKey = params.key
            val loadSize = params.loadSize

            val items = if(currentKey == null) {
                // Should not happen if initialKey is supplied in Pager, fallback to latest
                val latest = conversationLocalRepository.getLatestTimestamp(conversationId) ?: Clock.System.now().toEpochMilliseconds()
                conversationLocalRepository.getMessagesBefore(conversationId, latest, loadSize)
            } else {
                when(params) {
                    // Append = Loading items higher in the UI (older messages toward top)
                    is LoadParams.Append<*> -> {
                        conversationLocalRepository.getMessagesBefore(conversationId, currentKey, loadSize)
                    }
                    // Prepend = Loading items lower in the UI (newer messages toward bottom)
                    is LoadParams.Prepend<*> -> {
                        conversationLocalRepository.getMessagesAfter(conversationId, currentKey, loadSize)
                    }
                    // Refresh = Initial centered window load
                    is LoadParams.Refresh<*> -> {
                        val before = conversationLocalRepository.getMessagesBefore(
                            conversationId,
                            currentKey,
                            loadSize / 2
                        )
                        val after = conversationLocalRepository.getMessagesAfter(
                            conversationId,
                            currentKey,
                            loadSize / 2
                        )

                        (before + after).sortedByDescending { it.timestamp }
                    }
                }
            }

            LoadResult.Page(
                data = items,
                // prevKey goes DOWN (newer messages)
                prevKey = items.firstOrNull()?.timestamp?.toEpochMilliseconds(),
                // nextKey goes UP (older messages)
                nextKey = items.lastOrNull()?.timestamp?.toEpochMilliseconds()
            )
        } catch(e: Exception) {
            LoadResult.Error(e)
        }
    }
}