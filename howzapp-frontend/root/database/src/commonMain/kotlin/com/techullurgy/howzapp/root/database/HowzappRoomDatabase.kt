package com.techullurgy.howzapp.root.database

import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor
import com.techullurgy.howzapp.feature.chats.db.dao.ConversationDao
import com.techullurgy.howzapp.feature.chats.db.dao.ConversationMessageDao
import com.techullurgy.howzapp.feature.chats.db.dao.ConversationMutationDao
import com.techullurgy.howzapp.feature.chats.db.entities.ConversationEntity
import com.techullurgy.howzapp.feature.chats.db.entities.ConversationMessageEntity
import com.techullurgy.howzapp.feature.chats.db.entities.ConversationMutationEntity
import com.techullurgy.howzapp.feature.common.db.dao.FileUploadDao
import com.techullurgy.howzapp.feature.common.db.entities.FileUploadEntity

@Database(
    version = 1,
    entities = [
        ConversationEntity::class,
        ConversationMessageEntity::class,
        ConversationMutationEntity::class,
        FileUploadEntity::class
    ]
)
@ConstructedBy(HowzappDatabaseConstructor::class)
internal abstract class HowzappRoomDatabase : RoomDatabase() {
    abstract val fileUploadDao: FileUploadDao
    abstract val conversationDao: ConversationDao
    abstract val conversationMessageDao: ConversationMessageDao
    abstract val conversationMutationDao: ConversationMutationDao
}

@Suppress("KotlinNoActualForExpect", "NO_ACTUAL_FOR_EXPECT")
internal expect object HowzappDatabaseConstructor : RoomDatabaseConstructor<HowzappRoomDatabase> {
    override fun initialize(): HowzappRoomDatabase
}