package com.techullurgy.howzapp.feature.chats.presentation.impl.models

import kotlin.uuid.Uuid

sealed interface ListItem {
    val id: String

    sealed interface Separator: ListItem {

        override val id: String
            get() = Uuid.random().toString()

        data class DateSeparator(val date: String): Separator
        data class UnreadMessagesSeparator(val count: Int): Separator

        data class Combined(
            val separators: List<Separator>
        ): Separator
    }

    data class MessageListItem(
        val message: MessageUiItem,
    ): ListItem {
        override val id: String
            get() = message.messageId
    }
}