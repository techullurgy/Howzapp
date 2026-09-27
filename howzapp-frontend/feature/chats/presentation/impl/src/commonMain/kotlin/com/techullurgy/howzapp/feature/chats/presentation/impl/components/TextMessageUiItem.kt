package com.techullurgy.howzapp.feature.chats.presentation.impl.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.techullurgy.howzapp.core.presentation.components.AppText
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.TextMessage

@Composable
internal fun TextMessageUiItem(
    item: TextMessage,
    modifier: Modifier = Modifier
) {
    AppText(
        text = item.text,
        modifier = modifier
    )
}