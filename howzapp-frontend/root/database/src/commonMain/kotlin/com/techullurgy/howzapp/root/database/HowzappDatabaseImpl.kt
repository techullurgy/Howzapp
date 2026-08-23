package com.techullurgy.howzapp.root.database

import androidx.room3.withReadTransaction
import androidx.room3.withWriteTransaction
import com.techullurgy.howzapp.core.database.Database
import org.koin.core.annotation.Singleton

@Singleton(binds = [Database::class])
internal class HowzappDatabaseImpl internal constructor(
    private val howzappRoomDatabase: HowzappRoomDatabase
): Database {
    override suspend fun <R> withWriteTransaction(
        block: suspend () -> R
    ) {
        howzappRoomDatabase.withWriteTransaction {
            block()
        }
    }

    override suspend fun <R> withReadTransaction(
        block: suspend () -> R
    ) {
        howzappRoomDatabase.withReadTransaction {
            block()
        }
    }
}