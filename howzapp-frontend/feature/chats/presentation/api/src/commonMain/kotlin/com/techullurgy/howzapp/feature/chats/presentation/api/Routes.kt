package com.techullurgy.howzapp.feature.chats.presentation.api

import com.techullurgy.howzapp.core.navigation.AppNavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface ChatRoute: AppNavKey {

    @Serializable data object ConversationListRoute: ChatRoute

    @Serializable data class ConversationRoute(val conversationId: String): ChatRoute
}