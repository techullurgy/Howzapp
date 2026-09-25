package com.techullurgy.howzapp.root.database

import androidx.room3.ColumnTypeConverters
import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor
import com.techullurgy.howzapp.feature.chats.db.converters.ConversationColumnTypeConverters
import com.techullurgy.howzapp.feature.chats.db.dao.ConversationDao
import com.techullurgy.howzapp.feature.chats.db.dao.ConversationMessageDao
import com.techullurgy.howzapp.feature.chats.db.dao.ConversationMessageOutboxDao
import com.techullurgy.howzapp.feature.chats.db.dao.MessageUploadsDao
import com.techullurgy.howzapp.feature.chats.db.dao.PendingMessageAcksDao
import com.techullurgy.howzapp.feature.chats.db.entities.ConversationEntity
import com.techullurgy.howzapp.feature.chats.db.entities.ConversationMessageEntity
import com.techullurgy.howzapp.feature.chats.db.entities.ConversationMessageOutboxEntity
import com.techullurgy.howzapp.feature.chats.db.entities.MessageUploadsEntity
import com.techullurgy.howzapp.feature.chats.db.entities.PendingMessageAcksEntity

@Database(
    version = 1,
    entities = [
        ConversationEntity::class,
        ConversationMessageEntity::class,
        ConversationMessageOutboxEntity::class,
        PendingMessageAcksEntity::class,
        MessageUploadsEntity::class,
    ]
)
@ColumnTypeConverters(
    CommonColumnTypeConverters::class,
    ConversationColumnTypeConverters::class
)
@ConstructedBy(HowzappDatabaseConstructor::class)
internal abstract class HowzappRoomDatabase : RoomDatabase() {
    abstract val conversationDao: ConversationDao
    abstract val conversationMessageDao: ConversationMessageDao
    abstract val conversationMessageOutboxDao: ConversationMessageOutboxDao
    abstract val pendingMessageAcksDao: PendingMessageAcksDao
    abstract val messageUploadsDao: MessageUploadsDao
}

@Suppress("KotlinNoActualForExpect", "NO_ACTUAL_FOR_EXPECT")
internal expect object HowzappDatabaseConstructor : RoomDatabaseConstructor<HowzappRoomDatabase> {
    override fun initialize(): HowzappRoomDatabase
}