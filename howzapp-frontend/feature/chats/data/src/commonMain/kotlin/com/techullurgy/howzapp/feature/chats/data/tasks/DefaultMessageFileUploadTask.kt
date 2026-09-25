@file:Suppress("unused")

package com.techullurgy.howzapp.feature.chats.data.tasks

import com.techullurgy.howzapp.core.files.PlatformFileFactory
import com.techullurgy.howzapp.core.network.fileupload.FileUploadClient
import com.techullurgy.howzapp.core.network.fileupload.UploadFileRequest
import com.techullurgy.howzapp.core.network.fileupload.UploadFileState
import com.techullurgy.howzapp.core.network.fileupload.UploadId
import com.techullurgy.howzapp.core.network.http.NetworkRequestMethod
import com.techullurgy.howzapp.core.network.http.NetworkRequestParams
import com.techullurgy.howzapp.feature.chats.domain.api.models.MessageOutboxEntry
import com.techullurgy.howzapp.feature.chats.domain.api.models.MessageUploadStatus
import com.techullurgy.howzapp.feature.chats.domain.api.repositories.ConversationRepository
import com.techullurgy.howzapp.feature.chats.domain.api.tasks.MessageFileUploadTask
import kotlinx.coroutines.flow.transformLatest
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
internal class DefaultMessageFileUploadTask(
    private val conversationRepository: ConversationRepository,
    @Provided private val fileUploadClient: FileUploadClient
): MessageFileUploadTask {
    override suspend fun invoke(entry: MessageOutboxEntry) {
        val uploads = conversationRepository.findBatchInMessageUploadsPending(entry.batchId)

        val cancellationSignal = conversationRepository.isBatchCancelledInMessageOutbox(entry.batchId)
            .transformLatest { isCancelled ->
                if(isCancelled) {
                    emit(Unit)
                }
            }

        val uploadRequests = uploads.map {
            val platformFile = PlatformFileFactory.create(it.identifier)

            UploadFileRequest(
                uploadId = UploadId(it.uploadId),
                file = platformFile,
                initParams = NetworkRequestParams.WithoutBody(
                    method = NetworkRequestMethod.GET,
                    url = "/signing-url",
                    headers = mapOf(
                        "Content-Length" to platformFile.getSize()
                    )
                ),
                commitParams = NetworkRequestParams.WithoutBody(
                    method = NetworkRequestMethod.GET,
                    url = "/commit",
                )
            )
        }

        fileUploadClient.uploadFiles(
            requests = uploadRequests,
            cancellationSignal = cancellationSignal,
            onUpdate = {
                val updatedState = when(val state = it.state) {
                    UploadFileState.Cancelled -> MessageUploadStatus.Cancelled
                    is UploadFileState.Completed -> MessageUploadStatus.Completed(state.publicUrl)
                    is UploadFileState.Failed -> MessageUploadStatus.Failed(state.error)
                    UploadFileState.Initiated -> MessageUploadStatus.Initiated
                    is UploadFileState.Uploading -> MessageUploadStatus.Uploading(state.progress)
                }
                conversationRepository.updateMessageUploadState(it.uploadId.id, updatedState)
            }
        )
    }
}