package com.techullurgy.howzapp.core.database

interface Database {
    suspend fun <R> withWriteTransaction(block: suspend () -> R)
    suspend fun <R> withReadTransaction(block: suspend () -> R)
}