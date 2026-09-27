package com.techullurgy.howzapp.feature.chats.presentation.impl.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.AudioMessage
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.CallMessage
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.ContactMessage
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.DocumentMessage
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.GifMessage
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.ImageMessage
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.LocationMessage
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.PollMessage
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.StickerMessage
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.SystemMessage
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.TextMessage
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.VideoMessage
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.VoiceMessage
import com.techullurgy.howzapp.feature.chats.presentation.impl.models.MessageUiItem

@Composable
internal fun PersonMessageItem(
    message: MessageUiItem,
    anchorDirection: AnchorDirection?,
    modifier: Modifier = Modifier
) {
    require(message.content !is SystemMessage)

    MessageContainer(
        item = message,
        direction = anchorDirection,
        modifier = modifier
            .widthIn(max = 250.dp)
    ) {
        when (message.content) {
            is ImageMessage -> {
                ImageMessageUiItem(
                    item = message.content,
                    modifier = Modifier.width(200.dp)
                )
            }

            is TextMessage -> {
                TextMessageUiItem(
                    item = message.content,
                    modifier = Modifier.padding(4.dp)
                )
            }

            is VoiceMessage -> {
                VoiceMessageUiItem(
                    item = message.content,
                    owner = message.owner,
                    modifier = Modifier.padding(4.dp)
                )
            }

            is AudioMessage -> TODO()
            is CallMessage -> TODO()
            is ContactMessage -> TODO()
            is DocumentMessage -> TODO()
            is GifMessage -> TODO()
            is LocationMessage -> TODO()
            is PollMessage -> TODO()
            is StickerMessage -> TODO()
            is VideoMessage -> TODO()
        }
    }
}