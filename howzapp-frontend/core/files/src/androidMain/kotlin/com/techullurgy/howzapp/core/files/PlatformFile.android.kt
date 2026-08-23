package com.techullurgy.howzapp.core.files

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

sealed interface AndroidFile : PlatformFile {
    class Shared(
        private val uri: Uri,
        private val contentResolver: ContentResolver
    ) : AndroidFile {
        override suspend fun getSize(): Long = withContext(Dispatchers.IO) {
            var size = 0L
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val sizeIndex = it.getColumnIndex(OpenableColumns.SIZE)
                    if (sizeIndex != -1) size = it.getLong(sizeIndex)
                }
            }
            // Fallback for some custom FileProviders
            if (size == 0L) {
                try {
                    contentResolver.openAssetFileDescriptor(uri, "r")?.use { fd ->
                        size = fd.length
                    }
                } catch (e: Exception) { /* Ignore */ }
            }
            size
        }

        override suspend fun readChunks(chunkSize: Int, onChunk: suspend (ByteArray, Int) -> Unit) {
            streamChunks(
                chunkSize = chunkSize,
                streamProvider = {
                    contentResolver.openInputStream(uri)
                        ?: throw IllegalArgumentException("Cannot open stream")
                },
                onChunk = onChunk
            )
        }

        // Custom equality based solely on URI
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Shared) return false
            return uri == other.uri
        }

        override fun hashCode(): Int = uri.hashCode()

        override fun toString(): String = "AndroidFile.Shared(uri=$uri)"
    }

    class Internal(private val file: File) : AndroidFile {
        override suspend fun getSize(): Long = withContext(Dispatchers.IO) { file.length() }

        override suspend fun readChunks(chunkSize: Int, onChunk: suspend (ByteArray, Int) -> Unit) {
            streamChunks(
                chunkSize = chunkSize,
                streamProvider = { FileInputStream(file) },
                onChunk = onChunk
            )
        }
    }
}

// Reusable Android stream reader
private suspend fun streamChunks(chunkSize: Int, streamProvider: () -> InputStream, onChunk: suspend (ByteArray, Int) -> Unit) {
    withContext(Dispatchers.IO) {
        streamProvider().use {
            val buffer = ByteArray(chunkSize)
            while (currentCoroutineContext().isActive) {
                val bytesRead = it.read(buffer)
                if (bytesRead == -1) break
                onChunk(buffer, bytesRead)
            }
        }
    }
}