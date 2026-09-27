package com.techullurgy.howzapp.feature.chats.presentation.impl.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techullurgy.howzapp.core.presentation.components.AppText
import com.techullurgy.howzapp.core.presentation.theme.AppThemeProvider

@Composable
internal fun SystemBannerItem(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(color = AppThemeProvider.colors.systemMessageBackgroundColor)
            .padding(horizontal = 4.dp, vertical = 4.dp)
    ) {
        AppText(
            text = text,
            style = TextStyle(
                color = AppThemeProvider.colors.systemMessageTextColor,
                textAlign = TextAlign.Center,
                fontSize = 10.sp,
                lineHeight = 10.sp
            )
        )
    }
}