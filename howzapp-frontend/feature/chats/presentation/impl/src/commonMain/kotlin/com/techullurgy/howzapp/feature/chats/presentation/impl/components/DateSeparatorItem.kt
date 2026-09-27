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
internal fun DateSeparatorItem(
    item: ListItem.Separator.DateSeparator,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(color = AppThemeProvider.colors.separatorBackgroundColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        AppText(
            text = item.date,
            style = TextStyle(
                color = AppThemeProvider.colors.separatorTextColor,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}
/* background: linear-gradient(120deg, #d29628 4%, #8f36d9 55%, #4ec63f 92%);   */