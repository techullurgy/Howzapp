package com.techullurgy.howzapp.root.database.test.roomShared

import androidx.room3.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.techullurgy.howzapp.root.database.HowzappRoomDatabase

internal actual fun howzappDatabase(): HowzappRoomDatabase {
    return Room.inMemoryDatabaseBuilder<HowzappRoomDatabase>()
        .setDriver(BundledSQLiteDriver())
        .allowMainThreadQueries()
        .build()
}