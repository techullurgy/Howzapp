package com.techullurgy.howzapp.feature.chats.presentation.api.screens

import androidx.compose.runtime.Composable

interface IConversationScreen {
    @Composable operator fun invoke(conversationId: String)
}