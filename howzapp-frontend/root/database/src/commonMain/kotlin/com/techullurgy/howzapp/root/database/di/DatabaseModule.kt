@file:Suppress("unused")

package com.techullurgy.howzapp.root.database.di

import androidx.room3.RoomDatabase
import com.techullurgy.howzapp.feature.chats.db.dao.ConversationDao
import com.techullurgy.howzapp.feature.chats.db.dao.ConversationMessageDao
import com.techullurgy.howzapp.feature.chats.db.dao.ConversationMessageOutboxDao
import com.techullurgy.howzapp.feature.chats.db.dao.MessageUploadsDao
import com.techullurgy.howzapp.feature.chats.db.dao.PendingMessageAcksDao
import com.techullurgy.howzapp.root.database.HowzappRoomDatabase
import org.koin.core.annotation.Module
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Singleton
import org.koin.core.scope.Scope

@Module
internal expect class PlatformModule {
    @Singleton
    internal fun roomDatabaseBuilder(@Provided scope: Scope): RoomDatabase.Builder<HowzappRoomDatabase>
}

@Module
internal class DatabaseDaoModule {
    @Singleton internal fun conversationDao(db: HowzappRoomDatabase): ConversationDao = db.conversationDao
    @Singleton internal fun conversationMessageDao(db: HowzappRoomDatabase): ConversationMessageDao = db.conversationMessageDao
    @Singleton internal fun conversationMessageOutboxDao(db: HowzappRoomDatabase): ConversationMessageOutboxDao = db.conversationMessageOutboxDao
    @Singleton internal fun pendingMessageAcksDao(db: HowzappRoomDatabase): PendingMessageAcksDao = db.pendingMessageAcksDao
    @Singleton internal fun messageUploadsDao(db: HowzappRoomDatabase): MessageUploadsDao = db.messageUploadsDao
}

@Module(includes = [PlatformModule::class, DatabaseDaoModule::class])
class MainDatabaseModule {
    @Singleton
    internal fun howzappRoomDatabase(
        builder: RoomDatabase.Builder<HowzappRoomDatabase>
    ): HowzappRoomDatabase = builder.build()
}