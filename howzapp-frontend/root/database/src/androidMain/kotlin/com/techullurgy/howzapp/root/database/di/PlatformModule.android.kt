package com.techullurgy.howzapp.root.database.di

import android.content.Context
import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.techullurgy.howzapp.database.core.HowzappRoomDatabase
import kotlinx.coroutines.Dispatchers
import org.koin.core.annotation.Module
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Singleton
import org.koin.core.scope.Scope

@Module
actual class PlatformModule {
    @Singleton
    internal actual fun roomDatabaseBuilder(@Provided scope: Scope): RoomDatabase.Builder<HowzappRoomDatabase> {
        val context = scope.get<Context>()
        val appContext = context.applicationContext
        val dbFile = appContext.getDatabasePath("howzapp_room.db")
        return Room.databaseBuilder<HowzappRoomDatabase>(
            context = appContext,
            name = dbFile.absolutePath
        ).setDriver(BundledSQLiteDriver()).setQueryCoroutineContext(Dispatchers.IO)
    }
}