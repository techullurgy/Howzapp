package com.techullurgy.howzapp.feature.chats.presentation.impl.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.Grid
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techullurgy.howzapp.core.presentation.components.AppAsyncImage
import com.techullurgy.howzapp.core.presentation.components.AppText
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.ImageMessage

@Composable
internal fun ImageMessageUiItem(
    item: ImageMessage,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        ImageContent(
            urls = item.medias.map { it.url },
            modifier = Modifier.fillMaxWidth().aspectRatio(1f)
        )

        item.caption?.let { c ->
            c.takeIf { it.isNotBlank() }?.let { caption ->
                Spacer(Modifier.height(8.dp))
                AppText(text = caption, modifier = Modifier.padding(4.dp))
            }
        }
    }
}

@OptIn(ExperimentalGridApi::class)
@Composable
private fun ImageContent(
    urls: List<String>,
    modifier: Modifier = Modifier
) {
    val totalSize = urls.size
    Grid(
        config = {
            when(totalSize) {
                1 -> {
                    row(1f)
                    column(1f)
                }
                2 -> {
                    row(1f)
                    repeat(2) { column(1f / 2) }
                }
                3 -> {
                    row(1f)
                    repeat(3) { column(1f / 3) }
                }
                else -> {
                    repeat(2) { row(1f / 2) }
                    repeat(2) { column(1f / 2) }
                }
            }
            gap(4.dp)
        },
        modifier = modifier
    ) {
        val displayable = urls.take(4)
        val remainingCount = (urls.size - displayable.size).coerceAtLeast(0)

        displayable.forEachIndexed { index, url ->
            Box {
                AppAsyncImage(
                    url = url,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                if(index == 3 && remainingCount > 0) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(color = Color.Black.copy(alpha = 0.7f)),
                        contentAlignment = Alignment.Center
                    ) {
                        AppText(
                            text = "+$remainingCount",
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }
}

//@OptIn(ExperimentalCoilApi::class)
//@Preview
//@Composable
//private fun ImageMessageUiItemPreview(
//    @PreviewParameter(_root_ide_package_.com.example.composeplaygroundandroid.chats.components.ImageMessagePreviewParameterProvider::class) message: ChatMessageUiItem.ImageMessageItem
//) {
//    val coilPreviewHandler = AsyncImagePreviewHandler {
//        ColorImage(Color.Red.toArgb(), 300, 300)
//    }
//    CompositionLocalProvider(LocalAsyncImagePreviewHandler provides coilPreviewHandler) {
//        MessageContainer(
//            item = message,
//            direction = _root_ide_package_.com.example.composeplaygroundandroid.chats.components.AnchorDirection.LEFT,
//            modifier = Modifier.widthIn(max = 280.dp)
//        ) {
//            require(it is ChatMessageUiItem.ImageMessageItem)
//            ImageMessageUiItem(
//                item = it,
//                modifier = Modifier.width(200.dp)
//            )
//        }
//    }
//}
//
//private class ImageMessagePreviewParameterProvider: CollectionPreviewParameterProvider<ChatMessageUiItem.ImageMessageItem>(
//    collection = run {
//        val urlCount = listOf(1,2,3,4,5,6)
//        val captions = listOf(
//            null,
//            "This is too much",
//            LoremIpsum(10).values.joinToString(" ")
//        )
//
//        urlCount.flatMap { count ->
//            captions.map { caption ->
//                ChatMessageUiItem.ImageMessageItem(
//                    List(count) { "Image" },
//                    caption,
//                    "",
//                    Clock.System.now(),
//                    OwnerMetadata.Person.Other(
//                        "",
//                        "",
//                        "",
//                        OwnerMetadata.MessageReadStatus.READ,
//                        isOnline = false,
//                        hasStatusUpdates = false
//                    )
//                )
//            }
//        }
//    }
//)