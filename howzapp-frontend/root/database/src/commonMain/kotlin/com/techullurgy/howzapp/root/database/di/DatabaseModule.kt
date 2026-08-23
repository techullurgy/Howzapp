package com.techullurgy.howzapp.database.di

import androidx.room3.RoomDatabase
import com.techullurgy.howzapp.database.core.HowzappRoomDatabase
import com.techullurgy.howzapp.feature.chats.infra.dao.ConversationDao
import com.techullurgy.howzapp.feature.chats.infra.dao.ConversationMessageDao
import com.techullurgy.howzapp.feature.chats.infra.dao.ConversationMutationDao
import com.techullurgy.howzapp.feature.common.db.dao.FileUploadDao
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
    @Singleton internal fun fileUploadDao(db: HowzappRoomDatabase): FileUploadDao = db.fileUploadDao
    @Singleton internal fun conversationDao(db: HowzappRoomDatabase): ConversationDao = db.conversationDao
    @Singleton internal fun conversationMessageDao(db: HowzappRoomDatabase): ConversationMessageDao = db.conversationMessageDao
    @Singleton internal fun conversationMutationDao(db: HowzappRoomDatabase): ConversationMutationDao = db.conversationMutationDao
}

@Module(includes = [PlatformModule::class, DatabaseDaoModule::class])
class MainDatabaseModule {
    @Singleton
    internal fun howzappRoomDatabase(
        builder: RoomDatabase.Builder<HowzappRoomDatabase>
    ): HowzappRoomDatabase = builder.build()
}