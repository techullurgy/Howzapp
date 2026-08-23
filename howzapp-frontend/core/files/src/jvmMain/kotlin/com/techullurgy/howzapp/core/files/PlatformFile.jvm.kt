package com.techullurgy.howzapp.core.files

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream

class JvmFile(private val file: File) : PlatformFile {

    override suspend fun getSize(): Long = withContext(Dispatchers.IO) {
        file.length()
    }

    override suspend fun readChunks(chunkSize: Int, onChunk: suspend (ByteArray, Int) -> Unit) {
        withContext(Dispatchers.IO) {
            FileInputStream(file).use { stream ->
                val buffer = ByteArray(chunkSize)
                while (isActive) {
                    val bytesRead = stream.read(buffer)
                    if (bytesRead == -1) break
                    onChunk(buffer, bytesRead)
                }
            }
        }
    }
}