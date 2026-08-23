package com.techullurgy.howzapp.common.core.pubsub

import kotlinx.coroutines.flow.Flow
import java.time.Duration

interface IPubSubManager {
    fun listenTo(key: String): Flow<String>

    fun receive(keys: List<String>): Flow<String>

    suspend fun convertAndSend(destination: String, message: String): Long?

    suspend fun setAdd(key: String, values: Set<String>): Long?
    suspend fun setContains(key: String, value: String): Boolean?
    suspend fun setMembers(key: String): List<String>?

    suspend fun valueGet(key: String): String?
    suspend fun valueSet(key: String, value: String, timeout: Duration? = null): Boolean?
    suspend fun valueDelete(key: String): Boolean?

    suspend fun hashGet(key: String, hashKey: String): String?
    suspend fun hashPut(key: String, hashKey: String, value: String): Boolean?
    suspend fun hashPutAll(key: String, hashes: Map<String, String>): Boolean?
    suspend fun hashEntries(key: String): Map<String, String>?
}