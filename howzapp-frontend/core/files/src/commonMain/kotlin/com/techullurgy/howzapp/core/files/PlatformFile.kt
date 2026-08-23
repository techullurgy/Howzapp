package com.techullurgy.howzapp.core.files

interface PlatformFile {
    suspend fun getSize(): Long

    /**
     * Reads the file in memory-safe chunks natively.
     * Framework-agnostic: emits raw Kotlin ByteArrays.
     */
    suspend fun readChunks(
        chunkSize: Int,
        onChunk: suspend (buffer: ByteArray, bytesRead: Int) -> Unit
    )
}