package com.techullurgy.howzapp.core.files

import js.buffer.ArrayBuffer
import js.buffer.DataView
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.isActive
import kotlinx.coroutines.suspendCancellableCoroutine
import web.blob.Blob
import web.events.EventHandler
import web.file.File
import web.file.FileReader
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.js.ExperimentalWasmJsInterop

internal expect fun DataView<ArrayBuffer>.toByteArray(): ByteArray

@OptIn(ExperimentalWasmJsInterop::class)
class BrowserFile(private val file: File) : PlatformFile {
    override suspend fun getSize(): Long = file.size.toLong()

    override suspend fun readChunks(chunkSize: Int, onChunk: suspend (ByteArray, Int) -> Unit) {
        val chunkSize = chunkSize.toDouble()
        var offset = 0.0
        val totalSize = file.size

        while (offset < totalSize && currentCoroutineContext().isActive) {
            val end = (offset + chunkSize).coerceAtMost(totalSize)

            // Slice the Blob
            val slice = this.file.slice(offset, end)

            // Read as ArrayBuffer
            val arrayBuffer = slice.readAsArrayBuffer()

            // Delegate ByteArray allocation to actual functions
            val bytes = DataView(arrayBuffer).toByteArray()

            onChunk(bytes, arrayBuffer.byteLength)
            offset = end
        }
    }

    /**
     * Cross-platform (JS & Wasm) helper to read a Blob chunk as an ArrayBuffer.
     * Replaces the missing `Blob.arrayBuffer()` Promise.
     */
    private suspend fun Blob.readAsArrayBuffer(): ArrayBuffer = suspendCancellableCoroutine { cont ->
        val reader = FileReader()

        reader.onload = EventHandler {
            cont.resume(reader.result as ArrayBuffer)
        }

        reader.onerror = EventHandler {
            cont.resumeWithException(Exception("Failed to read file chunk in browser"))
        }

        reader.readAsArrayBuffer(this)
    }
}