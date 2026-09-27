package com.techullurgy.howzapp.feature.chats.presentation.impl.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalFlexBoxApi
import androidx.compose.foundation.layout.FlexAlignItems
import androidx.compose.foundation.layout.FlexBox
import androidx.compose.foundation.layout.FlexJustifyContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
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
import com.techullurgy.howzapp.feature.chats.presentation.impl.models.ListItem
import com.techullurgy.howzapp.feature.chats.presentation.impl.models.OwnerMetadata

@OptIn(ExperimentalFlexBoxApi::class)
@Composable
internal fun MessagesList(
    messages: LazyPagingItems<ListItem>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 8.dp)
    ) {
        item {
            FlexBox(
                config = {
                    alignItems(FlexAlignItems.Center)
                    justifyContent(FlexJustifyContent.Center)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                SystemBannerItem(
                    text = "The messages in this chat are end-to-end encrypted. Only you and the members of this chat can view the messages. Tap here for more info",
                    modifier = Modifier.fillParentMaxWidth(0.75f)
                )
            }
        }

        items(
            count = messages.itemCount,
            key = { messages.peek(it)!!.id },
            contentType = {
                decideContentType(
                    messages.peek(it)!!
                )
            }
        ) {index ->
            val current = messages[index]!! // No Placeholders (Assumption)
            val previous = if(index > 0) messages.peek(index - 1)!! else null

            val anchorDirection = previous?.let { decideAnchorDirection(previous, current) }

            Column(
                modifier = Modifier.fillMaxWidth().padding(bottom = 2.dp)
            ) {
                when(current) {
                    is ListItem.MessageListItem -> {
                        when(val owner = current.message.owner) {
                            is OwnerMetadata.Person -> {
                                val alignment = when(owner) {
                                    is OwnerMetadata.Person.Other -> Alignment.Start
                                    is OwnerMetadata.Person.You -> Alignment.End
                                }

                                val alignModifier = Modifier.align(alignment)

                                anchorDirection?.let { direction ->
                                    Row(
                                        modifier = Modifier.then(alignModifier).padding(top = 4.dp)
                                    ) {
                                        when(direction) {
                                            AnchorDirection.LEFT -> {
                                                ProfileMarkerItem(
                                                    profile = owner,
                                                    modifier = Modifier.size(40.dp)
                                                )
                                                Spacer(Modifier.width(6.dp))
                                                PersonMessageItem(
                                                    message = current.message,
                                                    anchorDirection = direction,
                                                )
                                            }
                                            AnchorDirection.RIGHT -> {
                                                PersonMessageItem(
                                                    message = current.message,
                                                    anchorDirection = direction,
                                                )
                                                Spacer(Modifier.width(6.dp))
                                                ProfileMarkerItem(
                                                    profile = owner,
                                                    modifier = Modifier.size(40.dp)
                                                )
                                            }
                                        }
                                    }
                                } ?: run {
                                    PersonMessageItem(
                                        message = current.message,
                                        anchorDirection = null,
                                        modifier = Modifier
                                            .then(alignModifier)
                                            .padding(horizontal = 40.dp)
                                    )
                                }

                            }
                            OwnerMetadata.System -> {
                                SystemMessageItem(
                                    item = current.message.content as SystemMessage,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                            }
                        }

                    }
                    is ListItem.Separator.Combined -> {
                        CombinedSeparatorItem(
                            item = current,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                                .padding(vertical = 6.dp)
                        )
                    }
                    is ListItem.Separator.DateSeparator -> {
                        DateSeparatorItem(
                            item = current,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                                .padding(vertical = 6.dp)
                        )
                    }
                    is ListItem.Separator.UnreadMessagesSeparator -> {
                        UnreadMessagesSeparatorItem(
                            item = current,
                            modifier = Modifier.align(Alignment.CenterHorizontally).padding(vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}


private fun decideAnchorDirection(
    previous: ListItem,
    current: ListItem
): AnchorDirection? {
    val isAnchorEnabled = previous.let { prev ->
        current.let { curr ->
            when {
                curr is ListItem.MessageListItem -> {
                    if(curr.message.owner is OwnerMetadata.Person) {
                        when {
                            prev !is ListItem.MessageListItem -> true
                            prev.message.owner !is OwnerMetadata.Person -> true
                            else -> {
                                val prevOwnerClass = prev.message.owner::class
                                val currOwnerClass = curr.message.owner::class

                                prevOwnerClass != currOwnerClass
                            }
                        }
                    } else false
                }
                else -> false
            }
        }
    }

    return if(isAnchorEnabled) {
        val owner = (current as ListItem.MessageListItem).message.owner
        when(owner) {
            is OwnerMetadata.Person.Other -> AnchorDirection.LEFT
            is OwnerMetadata.Person.You -> AnchorDirection.RIGHT
            else -> null
        }
    } else null
}

private fun decideContentType(item: ListItem): LazyItemContentType {
    return when(item) {
        is ListItem.MessageListItem -> {
            when(item.message.content) {
                is ImageMessage -> LazyItemContentType.MESSAGE_IMAGE
                is SystemMessage -> LazyItemContentType.MESSAGE_SYSTEM
                is TextMessage -> LazyItemContentType.MESSAGE_TEXT
                is VoiceMessage -> LazyItemContentType.MESSAGE_VOICE
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
        is ListItem.Separator.Combined -> LazyItemContentType.SEPARATOR_COMBINED
        is ListItem.Separator.DateSeparator -> LazyItemContentType.SEPARATOR_DATE
        is ListItem.Separator.UnreadMessagesSeparator -> LazyItemContentType.SEPARATOR_UNREAD_MESSAGES
    }
}

private enum class LazyItemContentType {
    SEPARATOR_COMBINED,
    SEPARATOR_DATE,
    SEPARATOR_UNREAD_MESSAGES,
    MESSAGE_SYSTEM,
    MESSAGE_TEXT,
    MESSAGE_IMAGE,
    MESSAGE_VOICE,
}