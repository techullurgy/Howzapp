package com.techullurgy.howzapp.core.presentation.components

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle

@Composable
fun AppText(
    text: String,
    style: TextStyle = LocalTextStyle.current,
    modifier: Modifier = Modifier
) {
    val resolvedStyle = LocalTextStyle.current.merge(style)

    Text(
        text = text,
        style = resolvedStyle,
        modifier = modifier
    )
}