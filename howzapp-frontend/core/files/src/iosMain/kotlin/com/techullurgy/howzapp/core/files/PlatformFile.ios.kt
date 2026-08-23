package com.techullurgy.howzapp.core.files

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.isActive
import platform.Foundation.*
import platform.posix.memcpy

@OptIn(ExperimentalForeignApi::class)
class AppleFile(val url: NSURL) : PlatformFile {

    override suspend fun getSize(): Long {
        val dict = NSFileManager.defaultManager.attributesOfItemAtPath(url.path!!, null)
        return dict?.get(NSFileSize) as? Long ?: 0L
    }

    override suspend fun readChunks(chunkSize: Int, onChunk: suspend (ByteArray, Int) -> Unit) {
        val fileHandle = NSFileHandle.fileHandleForReadingAtPath(url.path!!) ?: return
        try {
            val bufferSize = chunkSize.toULong()

            while (currentCoroutineContext().isActive) {
                val data: NSData = fileHandle.readDataOfLength(bufferSize)
                if (data.length == 0UL) break

                val bytes = ByteArray(data.length.toInt())
                bytes.usePinned { memcpy(it.addressOf(0), data.bytes, data.length) }

                onChunk(bytes, bytes.size)
            }
        } finally {
            fileHandle.closeFile()
        }
    }
}