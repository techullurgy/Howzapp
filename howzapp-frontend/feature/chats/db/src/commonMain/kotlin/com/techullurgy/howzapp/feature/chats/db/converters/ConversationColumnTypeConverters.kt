package com.techullurgy.howzapp.feature.chats.db.converters

import androidx.room3.ColumnTypeConverter
import com.techullurgy.howzapp.feature.chats.db.models.MessageContentStored
import com.techullurgy.howzapp.feature.chats.db.models.MessageReactionsStored
import com.techullurgy.howzapp.feature.chats.db.models.MessageUploadStatusStored
import kotlinx.serialization.json.Json
import kotlin.time.Instant

object ConversationColumnTypeConverters {
    @ColumnTypeConverter
    fun messageContentStoredToString(value: MessageContentStored?): String? {
        return value?.let {
            Json.encodeToString<MessageContentStored>(value)
        }
    }

    @ColumnTypeConverter
    fun stringToMessageContentStored(value: String?): MessageContentStored? {
        return value?.let {
            Json.decodeFromString<MessageContentStored>(value)
        }
    }

    @ColumnTypeConverter
    fun messageReactionsStoredToString(value: MessageReactionsStored?): String? {
        return value?.let {
            Json.encodeToString<MessageReactionsStored>(value)
        }
    }

    @ColumnTypeConverter
    fun stringToMessageReactionsStored(value: String?): MessageReactionsStored? {
        return value?.let {
            Json.decodeFromString<MessageReactionsStored>(value)
        }
    }

    @ColumnTypeConverter
    fun messageUploadStatusStoredToString(value: MessageUploadStatusStored?): String? {
        return value?.let {
            Json.encodeToString<MessageUploadStatusStored>(value)
        }
    }

    @ColumnTypeConverter
    fun stringToMessageUploadStatusStored(value: String?): MessageUploadStatusStored? {
        return value?.let {
            Json.decodeFromString<MessageUploadStatusStored>(value)
        }
    }
}