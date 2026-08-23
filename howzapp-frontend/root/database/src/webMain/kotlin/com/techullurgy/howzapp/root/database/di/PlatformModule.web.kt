package com.techullurgy.howzapp.root.database.di

@org.koin.core.annotation.Module
actual class PlatformModule {
    @org.koin.core.annotation.Singleton
    internal actual fun roomDatabaseBuilder(@org.koin.core.annotation.Provided scope: org.koin.core.scope.Scope): androidx.room3.RoomDatabase.Builder<com.techullurgy.howzapp.database.core.HowzappRoomDatabase> {
        TODO("Not yet implemented")
    }
}