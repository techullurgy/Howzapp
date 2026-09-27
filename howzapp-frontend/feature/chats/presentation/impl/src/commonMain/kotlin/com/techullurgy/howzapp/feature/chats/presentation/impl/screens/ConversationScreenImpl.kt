package com.techullurgy.howzapp.feature.chats.presentation.impl.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.techullurgy.howzapp.core.presentation.components.AppScaffold
import com.techullurgy.howzapp.core.presentation.theme.AppThemeProvider
import com.techullurgy.howzapp.feature.chats.presentation.api.screens.IConversationScreen
import com.techullurgy.howzapp.feature.chats.presentation.impl.components.ChatTopAppBar
import com.techullurgy.howzapp.feature.chats.presentation.impl.components.MessagesList
import com.techullurgy.howzapp.feature.chats.presentation.impl.models.OnlineStatus
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.Factory
import org.koin.core.parameter.parametersOf

@Factory
internal class IConversationScreenImpl: IConversationScreen {
    @Composable
    override operator fun invoke(conversationId: String) {
        ConversationScreen(conversationId)
    }
}

@Composable
private fun ConversationScreen(
    conversationId: String
) {
    val viewModel = koinViewModel<ConversationViewModel>(
        key = conversationId,
        parameters = { parametersOf(conversationId, null) }
    )

    val messages = viewModel.messages.collectAsLazyPagingItems()
    val state = viewModel.state.collectAsStateWithLifecycle()

    AppScaffold(
        topBar = {
            ChatTopAppBar(
                conversation = state.value.conversation
            )
        },
        containerColor = AppThemeProvider.colors.backgroundColor,
        modifier = Modifier
            .fillMaxSize()
    ) {
        MessagesList(
            messages = messages,
            modifier = Modifier.padding(it)
        )
    }
}