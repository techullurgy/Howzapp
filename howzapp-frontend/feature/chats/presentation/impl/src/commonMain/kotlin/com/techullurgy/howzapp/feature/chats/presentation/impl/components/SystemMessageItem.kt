package com.techullurgy.howzapp.feature.chats.presentation.impl.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.techullurgy.howzapp.core.presentation.theme.AppThemeProvider
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.SystemEvent
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.SystemMessage

@Composable
internal fun SystemMessageItem(
    item: SystemMessage,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(AppThemeProvider.colors.systemMessageBackgroundColor)
            .padding(4.dp)
    ) {
        when(item.event) {
            is SystemEvent.AdminPromoted -> TODO()
            is SystemEvent.AdminRemoved -> TODO()
            is SystemEvent.GroupIconChanged -> TODO()
            is SystemEvent.GroupNameChanged -> TODO()
            is SystemEvent.UserJoined -> TODO()
            is SystemEvent.UserLeft -> TODO()
        }
    }
}