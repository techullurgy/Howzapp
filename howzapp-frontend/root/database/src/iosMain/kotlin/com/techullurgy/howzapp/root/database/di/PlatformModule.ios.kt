package com.techullurgy.howzapp.root.database.di

import androidx.room3.Room
import androidx.room3.RoomDatabase
import com.techullurgy.howzapp.database.core.HowzappRoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.annotation.Module
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Singleton
import org.koin.core.scope.Scope
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@Module
actual class PlatformModule {
    @Singleton
    internal actual fun roomDatabaseBuilder(@Provided scope: Scope): RoomDatabase.Builder<HowzappRoomDatabase> {
        val dbFilePath = documentDirectory() + "/howzapp_room.db"
        return Room.databaseBuilder<HowzappRoomDatabase>(
            name = dbFilePath,
        ).setQueryCoroutineContext(Dispatchers.IO)
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory?.path)
}