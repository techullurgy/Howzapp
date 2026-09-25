package com.techullurgy.howzapp.root.database

import androidx.room3.ColumnTypeConverter
import kotlinx.serialization.json.Json
import kotlin.time.Instant

object CommonColumnTypeConverters {

    @ColumnTypeConverter
    fun instantToString(value: Instant?): String? {
        return value?.let {
            Json.encodeToString<Instant>(value)
        }
    }

    @ColumnTypeConverter
    fun stringToInstant(value: String?): Instant? {
        return value?.let {
            Json.decodeFromString<Instant>(value)
        }
    }
}