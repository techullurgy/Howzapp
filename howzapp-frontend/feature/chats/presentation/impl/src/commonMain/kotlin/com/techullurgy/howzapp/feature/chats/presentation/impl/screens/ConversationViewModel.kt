package com.techullurgy.howzapp.feature.chats.presentation.impl.screens

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.techullurgy.howzapp.feature.chats.domain.api.models.Conversation
import com.techullurgy.howzapp.feature.chats.domain.api.models.ConversationMessage
import com.techullurgy.howzapp.feature.chats.domain.api.models.MessageDeliveryStatus
import com.techullurgy.howzapp.feature.chats.domain.api.models.OutboxMessage
import com.techullurgy.howzapp.feature.chats.domain.api.usecases.ObserveConversationOutboxMessagesUseCase
import com.techullurgy.howzapp.feature.chats.domain.api.usecases.ObserveConversationUseCase
import com.techullurgy.howzapp.feature.chats.domain.api.usecases.ObserveForPagedMessagesUseCase
import com.techullurgy.howzapp.feature.chats.domain.api.usecases.ObserveUnreadMessagesCountUseCase
import com.techullurgy.howzapp.feature.chats.presentation.impl.models.ConversationUiItem
import com.techullurgy.howzapp.feature.chats.presentation.impl.models.ListItem
import com.techullurgy.howzapp.feature.chats.presentation.impl.models.MessageUiItem
import com.techullurgy.howzapp.feature.chats.presentation.impl.models.OwnerMetadata
import com.techullurgy.howzapp.feature.chats.presentation.impl.models.ParticipantInfoUiItem
import com.techullurgy.howzapp.feature.users.domain.api.models.User
import com.techullurgy.howzapp.feature.users.domain.api.models.UserExistType
import com.techullurgy.howzapp.feature.users.domain.api.models.UserId
import com.techullurgy.howzapp.feature.users.domain.api.models.UserOnlineStatus
import com.techullurgy.howzapp.feature.users.domain.api.usecases.ObtainUserFromUserIdUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel
import org.koin.core.annotation.Provided
import kotlin.math.abs
import kotlin.time.Clock
import kotlin.time.Instant

@KoinViewModel
internal class ConversationViewModel(
    @Provided obtainUserFromUserIdUseCase: ObtainUserFromUserIdUseCase,
    @Provided observeConversationUseCase: ObserveConversationUseCase,
    @Provided observeUnreadMessagesCountUseCase: ObserveUnreadMessagesCountUseCase,
    @Provided observeForPagedMessagesUseCase: ObserveForPagedMessagesUseCase,
    @Provided observeConversationOutboxMessagesUseCase: ObserveConversationOutboxMessagesUseCase,
    @InjectedParam conversationId: String,
    @InjectedParam initialKey: Long,
): ViewModel() {

    val state: StateFlow<ConversationUiState> field = MutableStateFlow(ConversationUiState())

    private val conversation: Flow<ConversationUiItem?> = observeConversationUseCase(conversationId)
        .map { conversation ->
            conversation?.let {
                when(conversation) {
                    is Conversation.Direct -> ConversationUiItem.Direct(
                        conversationId = conversationId,
                        to = obtainUserFromUserIdUseCase(UserId(conversation.to))!!
                    )
                    is Conversation.Group -> ConversationUiItem.Group(
                        conversationId = conversationId,
                        title = conversation.title,
                        avatarUrl = conversation.avatarUrl,
                        createdAt = conversation.createdAt,
                        participants = conversation.participants.map {
                            ParticipantInfoUiItem(
                                user = obtainUserFromUserIdUseCase(UserId(it.userId))!!,
                                joinedAt = it.joinedAt,
                                type = it.type
                            )
                        }
                    )
                }
            }
        }

    private val outboxMessages = observeConversationOutboxMessagesUseCase(conversationId)

    val messages = combine(
        observeUnreadMessagesCountUseCase(conversationId),
        observeForPagedMessagesUseCase(
            conversationId = conversationId,
            initialRefreshKey = initialKey
        )
    ) { unreadCount, pagedMessages ->
        pagedMessages
            .map<ConversationMessage, ListItem> {
                val user = obtainUserFromUserIdUseCase(it.senderId)
                ListItem.MessageListItem(
                    message = it.toMessageUiItem(user)
                )
            }.insertSeparators { before, after ->
                val isTop = before == null && after != null
                val isMiddle = before != null && after != null
                val isBottom = before != null && after == null

                val extractMessageReadStatusOrNull: (OwnerMetadata) -> OwnerMetadata.MessageReadStatus? = { (it as? OwnerMetadata.Person.Other)?.messageReadStatus }

                when {
                    isTop -> {
                        val top = after as ListItem.MessageListItem
                        if(extractMessageReadStatusOrNull(top.message.owner) == OwnerMetadata.MessageReadStatus.UNREAD) {
                            ListItem.Separator.Combined(
                                separators = listOf(
                                    ListItem.Separator.UnreadMessagesSeparator(unreadCount),
                                    ListItem.Separator.DateSeparator(
                                        instantFormatToString(top.message.timestamp)
                                    )
                                )
                            )
                        } else {
                            ListItem.Separator.DateSeparator(
                                instantFormatToString(top.message.timestamp)
                            )
                        }
                    }
                    isMiddle -> {
                        before as ListItem.MessageListItem
                        after as ListItem.MessageListItem

                        val beforeMessageReadStatus = extractMessageReadStatusOrNull(before.message.owner)
                        val afterMessageReadStatus = extractMessageReadStatusOrNull(after.message.owner)

                        val shouldShowUnreadSeparator = (beforeMessageReadStatus == null || beforeMessageReadStatus == OwnerMetadata.MessageReadStatus.READ)
                                && afterMessageReadStatus == OwnerMetadata.MessageReadStatus.UNREAD

                        val beforeMessageDate = before.message.timestamp.toLocalDateTime().date
                        val afterMessageDate = after.message.timestamp.toLocalDateTime().date

                        val shouldShowDateSeparator = beforeMessageDate != afterMessageDate

                        when {
                            shouldShowDateSeparator && shouldShowUnreadSeparator -> {
                                ListItem.Separator.Combined(
                                    separators = listOf(
                                        ListItem.Separator.UnreadMessagesSeparator(unreadCount),
                                        ListItem.Separator.DateSeparator(
                                            instantFormatToString(after.message.timestamp)
                                        )
                                    )
                                )
                            }
                            shouldShowDateSeparator -> {
                                ListItem.Separator.DateSeparator(
                                    instantFormatToString(after.message.timestamp)
                                )
                            }
                            shouldShowUnreadSeparator -> {
                                ListItem.Separator.UnreadMessagesSeparator(unreadCount)
                            }
                            else -> null
                        }
                    }
                    isBottom -> null
                    else -> null
                }
            }
    }.cachedIn(viewModelScope)

    init {
        combine(
            conversation,
            outboxMessages
        ) { conversation, outboxMessages ->
            state.update {
                it.copy(
                    conversation = conversation,
                    pendingMessages = outboxMessages
                )
            }
        }.launchIn(viewModelScope)
    }
}

@Immutable
data class ConversationUiState(
    val conversation: ConversationUiItem? = null,
    val pendingMessages: List<OutboxMessage> = emptyList()
)

private fun ConversationMessage.toMessageUiItem(user: User?): MessageUiItem {
    val isYou = user?.userExistType == UserExistType.OWNER

    val owner = when {
        senderId == UserId("SYSTEM") -> OwnerMetadata.System
        isYou -> OwnerMetadata.Person.You(
            name = "You",
            color = "",
            profileUrl = user.profileUrl,
            messageReceiverStatus = when (status) {
                MessageDeliveryStatus.SENT -> OwnerMetadata.MessageReceiverStatus.SENT
                MessageDeliveryStatus.DELIVERED -> OwnerMetadata.MessageReceiverStatus.RECEIVED
                MessageDeliveryStatus.READ -> OwnerMetadata.MessageReceiverStatus.READ
                null -> throw IllegalStateException()
            }
        )
        else -> OwnerMetadata.Person.Other(
            name = user?.displayName ?: "Anonymous",
            color = "",
            profileUrl = user?.profileUrl,
            messageReadStatus = when (isRead) {
                true -> OwnerMetadata.MessageReadStatus.READ
                false -> OwnerMetadata.MessageReadStatus.UNREAD
                null -> TODO()
            },
            isOnline = user?.onlineStatus == UserOnlineStatus.Online,
            hasStatusUpdates = false
        )
    }

    return MessageUiItem(
        id = id.id,
        conversationId = conversationId.id,
        seqNo = seqNo,
        owner = owner,
        content = content,
        timestamp = timestamp,
        reactions = reactions,
        replyTo = replyTo?.id,
        forwarded = forwarded,
        edited = edited,
        starred = starred,
        deleted = deleted
    )
}


private fun instantFormatToString(instant: Instant): String {
    val date = instant.toLocalDateTime().date
    val today = Clock.System.now().toLocalDateTime().date
    val dateDifference = date.daysUntil(today)
    if(dateDifference == 0) {
        return "Today"
    }
    if(abs(dateDifference) == 1) {
        return "Yesterday"
    }

    return date.format(
        LocalDate.Format {
            day()
            char(' ')
            monthName(MonthNames.ENGLISH_ABBREVIATED)
            if(date.year != today.year) {
                char(' ')
                year()
            }
        }
    )
}

private fun Instant.toLocalDateTime(): LocalDateTime = toLocalDateTime(TimeZone.currentSystemDefault())