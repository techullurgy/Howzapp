@file:Suppress("unused")

package com.techullurgy.howzapp.base.network.fileupload

import com.techullurgy.howzapp.core.files.PlatformFile
import com.techullurgy.howzapp.core.network.fileupload.FileUploadClient
import com.techullurgy.howzapp.core.network.fileupload.UploadFileRequest
import com.techullurgy.howzapp.core.network.fileupload.UploadFileState
import com.techullurgy.howzapp.core.network.fileupload.UploadFileUpdate
import com.techullurgy.howzapp.core.network.fileupload.UserTriggeredCancellation
import com.techullurgy.howzapp.core.network.http.NetworkConfig
import com.techullurgy.howzapp.core.network.http.NetworkRequestMethod
import com.techullurgy.howzapp.core.network.http.NetworkRequestParams
import com.techullurgy.howzapp.core.network.http.SafeNetworkRequest
import com.techullurgy.howzapp.core.network.http.safeNetworkFlow
import com.techullurgy.howzapp.core.qualifiers.NonLocalNetwork
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.content.OutgoingContent
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.writeFully
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import org.koin.core.annotation.Singleton

@Singleton
internal class KtorFileUploadClient(
    private val networkRequest: SafeNetworkRequest,
    @NonLocalNetwork private val nonLocalNetworkRequest: SafeNetworkRequest
): FileUploadClient {

    private val semaphore = Semaphore(4)

    override suspend fun uploadFiles(
        requests: List<UploadFileRequest>,
        cancellationSignal: Flow<Unit>,
        onUpdate: suspend (UploadFileUpdate) -> Unit
    ) {
        supervisorScope {
            val cancellationJob = launch {
                cancellationSignal.collect {
                    this@supervisorScope.cancel(UserTriggeredCancellation())
                }
            }

            try {
                val uploadJobs = requests.map { request ->
                    launch {
                        semaphore.withPermit {
                            uploadFile(
                                request = request,
                                onUpdate = onUpdate
                            )
                        }
                    }
                }
                uploadJobs.joinAll()
            } finally {
                cancellationJob.cancel()
            }
        }
    }

    private suspend fun uploadFile(
        request: UploadFileRequest,
        onUpdate: suspend (UploadFileUpdate) -> Unit
    ) {
        onUpdate(
            UploadFileUpdate(
                uploadId = request.uploadId,
                state = UploadFileState.Initiated
            )
        )

        val contentLength = request.file.getSize()

        with(networkRequest) {
            safeNetworkFlow<String>(
                params = request.initParams
            ).first()
                .onSuccess { signedUrl ->
                    val outgoingContentBody = request.file.toOutgoingContent(4*1024*1024, contentLength) { bytesSent, totalBytes ->
                        if (totalBytes > 0) {
                            val percentage = (bytesSent.toDouble() / totalBytes.toDouble()) * 100.0
                            onUpdate(
                                UploadFileUpdate(
                                    uploadId = request.uploadId,
                                    state = UploadFileState.Uploading(percentage.toFloat())
                                )
                            )
                        }
                    }

                    val contentType = outgoingContentBody.contentType!!

                    with(nonLocalNetworkRequest) {
                        safeNetworkFlow<String>(
                            config = NetworkConfig(maxRetries = 0),
                            params = NetworkRequestParams.WithBody(
                                url = signedUrl,
                                method = NetworkRequestMethod.PUT,
                                headers = mapOf(
                                    HttpHeaders.ContentType to contentType,
                                    HttpHeaders.Expect to "100-Continue"
                                ),
                                body = outgoingContentBody
                            )
                        ).first()
                            .onSuccess { publicUrl ->
                                with(networkRequest) {
                                    // Commit with the server
                                    safeNetworkFlow<String>(
                                        params = NetworkRequestParams.WithBody(
                                            url = request.commitParams.url,
                                            method = NetworkRequestMethod.POST,
                                            queryParams = request.commitParams.queryParams,
                                            headers = request.commitParams.headers,
                                            body = """{"publicUrl":$publicUrl}"""
                                        )
                                    ).first()
                                        .onSuccess {
                                            onUpdate(
                                                UploadFileUpdate(
                                                    uploadId = request.uploadId,
                                                    state = UploadFileState.Completed(it)
                                                )
                                            )
                                        }
                                }
                            }
                            .onFailure { error ->
                                // Notify the Server as well because of this failure... (if possible)

                                onUpdate(
                                    UploadFileUpdate(
                                        uploadId = request.uploadId,
                                        state = UploadFileState.Failed(error)
                                    )
                                )
                            }
                    }
                }
                .onFailure { error ->
                    onUpdate(
                        UploadFileUpdate(
                            uploadId = request.uploadId,
                            state = UploadFileState.Failed(error)
                        )
                    )
                }
        }
    }

    private fun PlatformFile.toOutgoingContent(
        chunkSize: Int,
        contentLength: Long,
        onProgress: suspend (bytesSent: Long, totalBytes: Long) -> Unit
    ): OutgoingContent = object: OutgoingContent.WriteChannelContent() {
        override val contentLength: Long = contentLength
        override val contentType: ContentType = ContentType.Application.OctetStream

        override suspend fun writeTo(channel: ByteWriteChannel) {
            var sentBytes = 0L
            val totalBytes = contentLength

            this@toOutgoingContent.readChunks(chunkSize) { buffer, bytesRead ->
                channel.writeFully(buffer, 0, bytesRead)
                sentBytes += bytesRead
                onProgress(sentBytes, totalBytes)
            }
        }
    }
}