package com.techullurgy.howzapp.feature.chats.presentation.impl.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.techullurgy.howzapp.feature.chats.presentation.api.screens.IConversationScreen
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

    val chatItems = viewModel.chatItems.collectAsLazyPagingItems()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(
            count = chatItems.itemCount,
            key = chatItems.itemKey { it.toUniqueKey() }
        ) { index ->
            when(val currentChatItem = chatItems[index]) {
                is ChatItem.Message -> MessageBox(currentChatItem)
                is ChatItem.Separator -> ChatSeparator(currentChatItem)
                null -> TODO("Chat Item cannot be null")
            }
        }
    }
}

@Composable
private fun MessageBox(
    message: ChatItem.Message,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        BasicText(
            text = message.message.content,
            style = TextStyle(fontSize = 35.sp, textAlign = TextAlign.Center)
        )
    }
}

@Composable
private fun ChatSeparator(
    separator: ChatItem.Separator,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        when {
            separator.date != null && separator.showUnread -> {
                // Both (Date & Unread) Separators
                BasicText("${separator.date}-Unread")
            }
            separator.date != null -> {
                // Date Separator
                BasicText(separator.date)
            }
            separator.showUnread -> {
                // Unread Separator
                BasicText("Unread")
            }
        }
    }
}