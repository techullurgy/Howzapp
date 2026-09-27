package com.techullurgy.howzapp.feature.chats.presentation.impl.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techullurgy.howzapp.core.presentation.components.AppIcon
import com.techullurgy.howzapp.core.presentation.components.AppText
import com.techullurgy.howzapp.core.presentation.theme.AppThemeProvider
import com.techullurgy.howzapp.core.presentation.theme.icons.double_tick
import com.techullurgy.howzapp.core.presentation.theme.icons.single_tick
import com.techullurgy.howzapp.feature.chats.presentation.impl.models.MessageUiItem
import com.techullurgy.howzapp.feature.chats.presentation.impl.models.OwnerMetadata
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime

@Composable
fun MessageContainer(
    item: MessageUiItem,
    direction: AnchorDirection?,
    modifier: Modifier = Modifier,
    message: @Composable (MessageUiItem) -> Unit
) {
    val containerColor = when(item.owner) {
        is OwnerMetadata.Person.Other -> AppThemeProvider.colors.messageCardOtherColor
        is OwnerMetadata.Person.You -> AppThemeProvider.colors.messageCardYouColor
        else -> Color.Unspecified
    }

    val mutedContentColor = when(item.owner) {
        is OwnerMetadata.Person.Other -> AppThemeProvider.colors.messageCardOtherMutedContentColor
        is OwnerMetadata.Person.You -> AppThemeProvider.colors.messageCardYouMutedContentColor
        else -> Color.Unspecified
    }

    val readTickColor = AppThemeProvider.colors.readTickColor

    Box(
        modifier = modifier
            .drawBehind {
                if (direction != null) {
                    drawOutline(
                        outline = BubbleShape(
                            direction
                        ).createOutline(size, LayoutDirection.Ltr, this),
                        color = containerColor
                    )
                } else {
                    drawRoundRect(
                        color = containerColor,
                        cornerRadius = CornerRadius(20f)
                    )
                }
            }
            .innerShadow(if(direction != null) BubbleShape(
                direction
            ) else RectangleShape) {
                spread = 8f
                alpha = 0.4f
                radius = 10f
                color = Color.Black
            }
            .padding(4.dp)
    ) {
        Layout(
            content = {
                Box {
                    message(item)
                }
                // Time + Status (if any)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    val timeString = item.timestamp.toLocalDateTime(TimeZone.currentSystemDefault()).time.format(TIME_FORMAT)

                    AppText(
                        text = timeString,
                        style = TextStyle(
                            fontSize = 10.sp,
                            color = mutedContentColor,
                            lineHeight = 10.sp
                        )
                    )
                    if(item.owner is OwnerMetadata.Person.You) {
                        Spacer(Modifier.width(4.dp))
                        MessageReceiverStatusIcon(
                            status = item.owner.messageReceiverStatus,
                            mutedContentColor = mutedContentColor,
                            readTickColor = readTickColor
                        )
                    }
                }
            }
        ) { measurables, constraints ->
            val mainContent = measurables[0].measure(constraints)
            val subContentConstraints = constraints.copy(
                minWidth = 0,
                maxWidth = mainContent.width
            )
            val subContent = measurables[1].measure(subContentConstraints)
            val totalWidth = maxOf(mainContent.width, subContent.width)
            val totalHeight = mainContent.height + subContent.height
            layout(totalWidth, totalHeight) {
                mainContent.place(0, 0)
                subContent.place(0, mainContent.height)
            }
        }
    }
}

class BubbleShape(private val direction: AnchorDirection): Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = with(density) {
            Path().apply {
                when (direction) {
                    AnchorDirection.LEFT -> {
                        moveTo((-10).dp.toPx(), 0f)
                        lineTo(size.width, 0f)
                        lineTo(size.width, size.height)
                        lineTo(0f, size.height)
                        lineTo(0f, 10.dp.toPx())
                        close()
                    }

                    AnchorDirection.RIGHT -> {
                        moveTo(size.width + 10.dp.toPx(), 0f)
                        lineTo(0f, 0f)
                        lineTo(0f, size.height)
                        lineTo(size.width, size.height)
                        lineTo(size.width, 10.dp.toPx())
                        close()
                    }
                }
            }
        }
        return Outline.Generic(path = path)
    }
}

@Composable
private fun MessageReceiverStatusIcon(
    status: OwnerMetadata.MessageReceiverStatus,
    mutedContentColor: Color,
    readTickColor: Color,
) {
    Box(
        modifier = Modifier.size(12.dp)
    ) {
        when(status) {
            OwnerMetadata.MessageReceiverStatus.SENT -> {
                AppIcon(
                    imageVector = single_tick,
                    contentDescription = null,
                    tint = mutedContentColor
                )
            }
            OwnerMetadata.MessageReceiverStatus.RECEIVED -> {
                AppIcon(
                    imageVector = double_tick,
                    contentDescription = null,
                    tint = mutedContentColor
                )
            }
            OwnerMetadata.MessageReceiverStatus.READ -> {
                AppIcon(
                    imageVector = double_tick,
                    contentDescription = null,
                    tint = readTickColor
                )
            }
        }
    }
}

enum class AnchorDirection {
    LEFT, RIGHT
}

//@Preview
//@Composable
//private fun MessageContainerPreview(
//    @PreviewParameter(ChatItemPreviewParameterProvider::class) item: MessageUiItem
//) {
//    ComposePlaygroundAndroidTheme {
//        Box(
//            modifier = Modifier.padding(40.dp)
//        ) {
//            MessageContainer(
//                item = item,
//                direction = when(item.owner) {
//                    is OwnerMetadata.Person.Other -> AnchorDirection.LEFT
//                    is OwnerMetadata.Person.You -> AnchorDirection.RIGHT
//                    else -> TODO()
//                },
//                modifier = Modifier
//                    .widthIn(max = 200.dp)
//            ) {
//                when(it) {
//                    is MessageUiItem.ImageMessageItem -> Box(
//                        modifier = Modifier.fillMaxWidth().height(200.dp).background(Color.Red)
//                    )
//                    is MessageUiItem.SystemMessageItem -> TODO()
//                    is MessageUiItem.TextMessageItem -> Text(it.text, lineHeight = 18.sp)
//                    else -> {}
//                }
//            }
//        }
//    }
//}

val TIME_FORMAT = LocalTime.Format {
    amPmHour()
    char(':')
    minute()
    char(' ')
    amPmMarker("AM", "PM")
}

//class ChatItemPreviewParameterProvider: PreviewParameterProvider<MessageUiItem> {
//    override val values: Sequence<MessageUiItem> by lazy {
//
//        val statuses = OwnerMetadata.MessageReceiverStatus.entries.take(1)
//        val names = listOf("Irsath Kareem", "Rajathi")
//
//        val wordCounts = listOf(1,4,10)
//        val textMessages = statuses.flatMap { status ->
//            names.flatMap { name ->
//                wordCounts.map { count ->
//                    MessageUiItem.TextMessageItem(
//                        text = LoremIpsum(count).values.joinToString(" "),
//                        timestamp = Instant.fromEpochMilliseconds(System.currentTimeMillis()),
//                        messageId = Uuid.random().toString(),
//                        owner = OwnerMetadata.Person.You(
//                            name = name,
//                            color = "Color.Blue",
//                            profileUrl = null,
//                            messageReceiverStatus = status
//                        )
//                    )
//                }
//            }
//        }
//
//        val urlCounts = listOf(1,1,3,6)
//        val imageMessages = statuses.flatMap { status ->
//            names.flatMap { name ->
//                urlCounts.map { count ->
//                    MessageUiItem.ImageMessageItem(
//                        urls = List(count) { "https://images.com/img.png" },
//                        messageId = Uuid.random().toString(),
//                        timestamp = Instant.fromEpochMilliseconds(System.currentTimeMillis()),
//                        owner = OwnerMetadata.Person.You(
//                            name = name,
//                            color = "Color.Blue",
//                            profileUrl = null,
//                            messageReceiverStatus = status
//                        )
//                    )
//                }
//            }
//        }
//
//        (textMessages + imageMessages).asSequence()
//    }
//}