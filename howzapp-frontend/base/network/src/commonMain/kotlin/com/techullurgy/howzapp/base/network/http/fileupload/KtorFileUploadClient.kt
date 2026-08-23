package com.techullurgy.howzapp.base.network.http.fileupload

import com.techullurgy.howzapp.core.files.PlatformFile
import com.techullurgy.howzapp.core.network.http.core.NetworkRequestParams
import com.techullurgy.howzapp.core.network.http.fileupload.FileUploadClient
import com.techullurgy.howzapp.core.network.http.fileupload.FileUploadStatus
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.content.OutgoingContent
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.writeFully
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import org.koin.core.annotation.Singleton
import kotlin.collections.component1
import kotlin.collections.component2

@Singleton(binds = [FileUploadClient::class])
internal class KtorFileUploadClient(
    private val client: HttpClient
): FileUploadClient {
    override fun uploadFile(
        params: NetworkRequestParams.WithoutBody,
        file: PlatformFile,
        chunkSize: Int
    ): Flow<FileUploadStatus> = channelFlow {
        try {
            send(FileUploadStatus.Progress(0.0))

            client.post(
                params.url
            ) {
                params.queryParams?.forEach { (key, value) -> parameter(key, value) }
                params.headers?.forEach { (key, value) -> header(key, value) }

                val outgoingContentBody = file.toOutgoingContent(chunkSize) { bytesSent, totalBytes ->
                    if (totalBytes > 0) {
                        val percentage = (bytesSent.toDouble() / totalBytes.toDouble()) * 100.0
                        send(FileUploadStatus.Progress(percentage))
                    }
                }
                setBody(outgoingContentBody)
            }
            send(FileUploadStatus.Success)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            send(FileUploadStatus.Error(e))
        }
    }
}

private fun PlatformFile.toOutgoingContent(
    chunkSize: Int,
    onProgress: suspend (bytesSent: Long, totalBytes: Long) -> Unit
): OutgoingContent = object : OutgoingContent.WriteChannelContent() {
    // contentLength is omitted (null) so Ktor uses Chunked Transfer Encoding.
    // This is required because getSize() is now a suspend function.
    // override val contentLength: Long = ....

    override suspend fun writeTo(channel: ByteWriteChannel) {
        var sentBytes = 0L
        val totalBytes = getSize()

        // Consume the pure Kotlin byte chunks and push them into Ktor's stream
        this@toOutgoingContent.readChunks(chunkSize) { buffer, bytesRead ->
            channel.writeFully(buffer, 0, bytesRead)
            sentBytes += bytesRead
            onProgress(sentBytes, totalBytes)
        }
    }
}