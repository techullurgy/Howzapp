package com.techullurgy.howzapp.root.database.di

import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.web.WebWorkerSQLiteDriver
import com.techullurgy.howzapp.root.database.HowzappRoomDatabase
import org.koin.core.annotation.Module
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Singleton
import org.koin.core.scope.Scope

@Module
actual class PlatformModule {
    @Singleton
    internal actual fun roomDatabaseBuilder(@Provided scope: Scope): RoomDatabase.Builder<HowzappRoomDatabase> {
        return Room.databaseBuilder<HowzappRoomDatabase>("howzapp_room.db")
            .setDriver(webWorkerSQLiteDriver())
    }
}

expect fun webWorkerSQLiteDriver(): WebWorkerSQLiteDriver