package com.techullurgy.howzapp.feature.chats.presentation.impl.components

import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.withSaveLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techullurgy.howzapp.core.presentation.components.AppAsyncImage
import com.techullurgy.howzapp.core.presentation.components.AppIcon
import com.techullurgy.howzapp.core.presentation.components.AppText
import com.techullurgy.howzapp.core.presentation.theme.AppThemeProvider
import com.techullurgy.howzapp.core.presentation.theme.icons.call
import com.techullurgy.howzapp.core.presentation.theme.icons.more_vert
import com.techullurgy.howzapp.core.presentation.theme.icons.videocam
import com.techullurgy.howzapp.feature.chats.presentation.impl.models.ConversationUiItem
import com.techullurgy.howzapp.feature.chats.presentation.impl.models.OnlineStatus
import com.techullurgy.howzapp.feature.users.domain.api.models.UserOnlineStatus
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
internal fun ChatTopAppBar(
    conversation: ConversationUiItem?,
    modifier: Modifier = Modifier
) {
    if(conversation == null) return

    val transientOnlineStatus: OnlineStatus? = null
    val (title, profilePicture, onlineStatus) = provideTopBarData(conversation, transientOnlineStatus)

    val isOnline = onlineStatus == OnlineStatus.Online

    val subtitleText = when(onlineStatus) {
        is OnlineStatus.LastSeen -> "Last seen at ${onlineStatus.instant}"
        OnlineStatus.None -> null
        OnlineStatus.Online -> "Online"
        OnlineStatus.RecordingAudio -> "Recording audio..."
        OnlineStatus.Typing -> "typing..."
    }

    Box(
        modifier = modifier
            .background(AppThemeProvider.colors.topBarBackgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ProfilePicture
            HeaderProfilePicture(
                pictureUrl = profilePicture,
                isOnline = isOnline,
                modifier = Modifier
                    .size(60.dp)
            )

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                AppText(title, style = TextStyle(fontWeight = FontWeight.Bold))
                subtitleText?.let { subtitle ->
                    val canMarquee = onlineStatus is OnlineStatus.LastSeen
                    AppText(
                        text = subtitle,
                        style = TextStyle(fontSize = 14.sp),
                        modifier = Modifier
                            .then(
                                if(canMarquee) {
                                    Modifier
                                        .basicMarquee(
                                            iterations = 1,
                                            animationMode = MarqueeAnimationMode.Immediately,
                                            velocity = 40.dp
                                        )
                                } else Modifier
                            )
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                AppIcon(
                    imageVector = videocam,
                    contentDescription = null,
                )

                AppIcon(
                    imageVector = call,
                    contentDescription = null,
                )

                AppIcon(
                    imageVector = more_vert,
                    contentDescription = null,
                )
            }
        }
    }
}

@Composable
private fun HeaderProfilePicture(
    pictureUrl: String?,
    isOnline: Boolean,
    modifier: Modifier = Modifier
) {
    val colors = AppThemeProvider.colors

    Box(
        modifier = modifier
            .drawWithContent {
                if (isOnline) {
                    drawWithOnlineIndicator(6.dp.toPx(), colors.onlineIndicatorColor)
                } else drawContent()
            }
            .clip(CircleShape)
    ) {
        pictureUrl?.let {
            AppAsyncImage(
                url = pictureUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

private fun ContentDrawScope.drawWithOnlineIndicator(
    radius: Float,
    indicatorColor: Color
) {
    drawIntoCanvas { canvas ->
        canvas.withSaveLayer(
            bounds = size.toRect(),
            paint = Paint()
        ) {
            drawContent()

            val degree = 45f
            val x = (size.width / 2f) * cos(degree * (PI/180.0)).toFloat()
            val y = (size.width / 2f) * sin(degree * (PI/180.0)).toFloat()

            withTransform(
                transformBlock = {
                    translate(center.x, center.y)
                }
            ) {
                drawCircle(
                    color = Color.Black,
                    radius = radius + 2.dp.toPx(),
                    center = Offset(x,y),
                    blendMode = BlendMode.Clear
                )

                drawCircle(
                    color = indicatorColor,
                    radius = radius,
                    center = Offset(x,y)
                )
            }
        }
    }
}

private fun provideTopBarData(
    conversation: ConversationUiItem,
    onlineStatus: OnlineStatus? // Typing, RecordingAudio
): TopBarData {
    return when(conversation) {
        is ConversationUiItem.Direct -> TopBarData(
            title = conversation.to.displayName,
            profilePicture = conversation.to.profileUrl,
            onlineStatus = onlineStatus
                ?: when(val userOnlineStatus = conversation.to.onlineStatus) {
                    is UserOnlineStatus.LastSeen -> OnlineStatus.LastSeen(userOnlineStatus.lastSeenAt)
                    UserOnlineStatus.None -> OnlineStatus.None
                    UserOnlineStatus.Online -> OnlineStatus.Online
                }
        )
        is ConversationUiItem.Group -> TopBarData(
            title = conversation.title,
            profilePicture = conversation.avatarUrl,
            onlineStatus = onlineStatus ?: OnlineStatus.None
        )
    }
}

private data class TopBarData(
    val title: String,
    val profilePicture: String?,
    val onlineStatus: OnlineStatus
)