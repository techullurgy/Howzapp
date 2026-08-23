package com.techullurgy.howzapp.feature.chats.data.repos

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.techullurgy.howzapp.feature.chats.data.handlers.MessageResponseHandler
import com.techullurgy.howzapp.feature.chats.data.paging.ConversationPagingSource
import com.techullurgy.howzapp.feature.chats.data.paging.ConversationRemoteMediator
import com.techullurgy.howzapp.feature.chats.domain.api.models.ConversationMessage
import com.techullurgy.howzapp.feature.chats.domain.api.repositories.ConversationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Singleton

@Singleton
internal class ConversationRepositoryImpl(
    @Provided private val conversationLocalRepository: ConversationLocalRepository,
    private val conversationApiRepository: ConversationApiRepository,
    private val messageResponseHandler: MessageResponseHandler,
    @Provided private val coroutineScope: CoroutineScope
): ConversationRepository {
    @OptIn(ExperimentalPagingApi::class)
    override fun observeForMessages(
        conversationId: String,
        initialRefreshKey: Long
    ): Flow<PagingData<ConversationMessage>> {
        return Pager(
            config = PagingConfig(
                pageSize = 60,
                prefetchDistance = 5,
                initialLoadSize = 60
            ),
            initialKey = initialRefreshKey,
            remoteMediator = ConversationRemoteMediator(
                conversationId = conversationId,
                refreshTimestamp = initialRefreshKey,
                conversationLocalRepository = conversationLocalRepository,
                apiService = conversationApiRepository,
                messageResponseHandler = messageResponseHandler
            ),
            pagingSourceFactory = {
                ConversationPagingSource(
                    conversationId = conversationId,
                    conversationLocalRepository = conversationLocalRepository,
                    coroutineScope = coroutineScope
                )
            }
        ).flow
    }

    override suspend fun obtainFirstUnreadMessageTimestamp(conversationId: String): Long? {
        return conversationLocalRepository.getFirstUnreadMessageTimestamp(conversationId)
    }

    override suspend fun obtainOriginalMessage(
        conversationId: String,
        messageId: String
    ): ConversationMessage? {
        TODO("Not yet implemented")
    }
}