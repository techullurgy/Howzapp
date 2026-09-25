package com.techullurgy.howzapp.core.database

interface Database {
    suspend fun <R> withWriteTransaction(block: suspend () -> R): R
    suspend fun <R> withReadTransaction(block: suspend () -> R): R
}