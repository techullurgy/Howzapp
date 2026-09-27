package com.techullurgy.howzapp.feature.chats.presentation.impl.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.techullurgy.howzapp.core.presentation.components.AppAsyncImage
import com.techullurgy.howzapp.feature.chats.presentation.impl.models.OwnerMetadata

@Composable
internal fun ProfileMarkerItem(
    profile: OwnerMetadata.Person,
    modifier: Modifier = Modifier
) {
//    val isOnline = when(profile) {
//        is OwnerMetadata.Person.Other -> profile.isOnline
//        is OwnerMetadata.Person.You -> true
//    }

    val hasStatusUpdates = when(profile) {
        is OwnerMetadata.Person.Other -> profile.hasStatusUpdates
        is OwnerMetadata.Person.You -> false
    }

    Box(
        modifier = modifier
            .drawBehind {
                if(hasStatusUpdates) {
                    drawCircle(
                        brush = SolidColor(Color.Blue),
                        style = Stroke(width = 3.dp.toPx())
                    )
                }
            }
            .clip(CircleShape)
            .padding(3.dp)
    ) {
        profile.profileUrl?.let { url ->
            AppAsyncImage(
                url = url,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } ?: run {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .drawBehind {
                        val color = when(profile) {
                            is OwnerMetadata.Person.Other -> Color.Magenta
                            is OwnerMetadata.Person.You -> Color.Cyan
                        }

                        drawCircle(color)
                    }
            )
        }
    }
}