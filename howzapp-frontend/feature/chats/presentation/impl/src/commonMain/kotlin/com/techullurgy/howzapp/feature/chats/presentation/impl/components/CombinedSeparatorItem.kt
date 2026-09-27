package com.techullurgy.howzapp.feature.chats.presentation.impl.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.techullurgy.howzapp.feature.chats.presentation.impl.models.ListItem


@Composable
internal fun CombinedSeparatorItem(
    item: ListItem.Separator.Combined,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item.separators.forEach { separator ->
            require(separator !is ListItem.Separator.Combined)
            when(separator) {
                is ListItem.Separator.DateSeparator -> DateSeparatorItem(item = separator)
                is ListItem.Separator.UnreadMessagesSeparator -> UnreadMessagesSeparatorItem(item = separator)
            }
        }
    }
}