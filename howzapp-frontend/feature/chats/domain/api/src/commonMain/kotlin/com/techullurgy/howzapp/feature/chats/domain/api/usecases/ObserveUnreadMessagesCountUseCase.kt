package com.techullurgy.howzapp.feature.chats.domain.api.usecases

import kotlinx.coroutines.flow.Flow

interface ObserveUnreadMessagesCountUseCase {
    operator fun invoke(conversationId: String): Flow<Int>
}