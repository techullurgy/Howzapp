package com.techullurgy.howzapp.feature.chats.presentation.impl.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.techullurgy.howzapp.feature.chats.domain.api.models.ConversationMessage
import com.techullurgy.howzapp.feature.chats.domain.api.models.ConversationMessageStatus
import com.techullurgy.howzapp.feature.chats.domain.api.repositories.ConversationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel
import org.koin.core.annotation.Provided
import kotlin.time.Clock

@KoinViewModel
internal class ConversationViewModel(
    @InjectedParam private val conversationId: String,
    @InjectedParam private val targetTimestampKey: Long? = null, // Deep Link (Target)
    @Provided private val conversationRepository: ConversationRepository,
): ViewModel() {
    private val anchorTimestamp = MutableStateFlow<Long?>(null)

    val chatItems: Flow<PagingData<ChatItem>> = anchorTimestamp
        .filterNotNull()
        .flatMapLatest { refreshTimestamp ->
            conversationRepository.observeForMessages(
                conversationId = conversationId,
                initialRefreshKey = refreshTimestamp
            )
        }
        .map { pagingData ->
            pagingData
                .map<ConversationMessage, ChatItem> {
                    ChatItem.Message(it)
                }
                .insertSeparators { before, after ->
                    val afterMessage = (after as? ChatItem.Message)?.message
                        ?: return@insertSeparators null

                    val beforeMessage = (before as? ChatItem.Message)?.message

                    val afterDate = afterMessage.timestamp // .toDate()
                    val beforeDate = beforeMessage?.timestamp // .toDate()
                    val showDate = before == null || beforeDate != afterDate

                    // TODO: Read Status Check Logic needs to be changed, based on received message, not sent message
                    val beforeIsRead = (beforeMessage == null) || beforeMessage.status == ConversationMessageStatus.READ
                    val afterIsRead = after.message.status == ConversationMessageStatus.READ
                    // Transition:
                    // read message -> unread message
                    val showUnread = beforeIsRead && !afterIsRead

                    if(showDate || showUnread) {
                        ChatItem.Separator(
                            date = if(showDate) afterDate.toString() else null,
                            showUnread = showUnread
                        )
                    } else null
                }
        }
        .cachedIn(viewModelScope)


    init {
        determineInitialAnchorTimestamp()
    }

    private fun determineInitialAnchorTimestamp() {
        viewModelScope.launch {
            val desiredKey = targetTimestampKey
                ?: conversationRepository.obtainFirstUnreadMessageTimestamp(conversationId)
                ?: Clock.System.now().toEpochMilliseconds()

            anchorTimestamp.update { desiredKey }
        }
    }
}

sealed interface ChatItem {
    data class Message(
        val message: ConversationMessage
    ): ChatItem

    data class Separator(
        val date: String?,
        val showUnread: Boolean
    ): ChatItem {
        init {
            require(date != null || showUnread) {
                "Must (either one (OR) Both) of them is available as Separator (Date & Unread)"
            }
        }
    }

    fun toUniqueKey(): String {
        return when(this) {
            is Message -> "${message.id}#${message.timestamp}"
            is Separator -> {
                if(date != null && showUnread) {
                    "$date#Unread"
                } else date ?: if (showUnread) {
                    "Unread"
                } else ""
            }
        }
    }
}


// UI Model
sealed interface ChatItemUiModel {

    data class Outgoing(
        val message: ConversationMessage
    ) : ChatItemUiModel

    data class Incoming(
        val message: ConversationMessage,
        val sender: User
    ) : ChatItemUiModel

    data class DateSeparator(
        val text: String
    ) : ChatItemUiModel

    data class UnreadSeparator(
        val count: Int
    ) : ChatItemUiModel

    data class TypingIndicator(
        val user: User
    ) : ChatItemUiModel
}

fun ConversationMessage.toUiModel(
    currentUserId: UserId,
    sender: User
): ChatItemUiModel =
    if (senderId == currentUserId) {
        ChatItemUiModel.Outgoing(this)
    } else {
        ChatItemUiModel.Incoming(this, sender)
    }