package com.techullurgy.howzapp.feature.chats.presentation.impl.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.techullurgy.howzapp.core.presentation.components.AppText
import com.techullurgy.howzapp.core.presentation.theme.AppThemeProvider
import com.techullurgy.howzapp.feature.chats.presentation.impl.models.ListItem

@Composable
internal fun UnreadMessagesSeparatorItem(
    item: ListItem.Separator.UnreadMessagesSeparator,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(color = AppThemeProvider.colors.separatorBackgroundColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        AppText(
            text = "${item.count} Unread Messages",
            style = TextStyle(
                color = AppThemeProvider.colors.separatorTextColor,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}