package com.techullurgy.howzapp.feature.chats.domain.impl.tasks

import com.techullurgy.howzapp.feature.chats.domain.api.models.MessageOutboxEntry
import com.techullurgy.howzapp.feature.chats.domain.api.models.MessageOutboxStatus
import com.techullurgy.howzapp.feature.chats.domain.api.models.MessageUploadStatus
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.AudioMessage
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.DocumentMessage
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.GifMessage
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.ImageMessage
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.SystemMessage
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.VideoMessage
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.VoiceMessage
import com.techullurgy.howzapp.feature.chats.domain.api.repositories.ConversationRepository
import com.techullurgy.howzapp.feature.chats.domain.api.tasks.MessageFileUploadTask
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive

class PendingToReadyOutboxConverterTask(
    private val conversationRepository: ConversationRepository,
    private val messageFileUploadTask: MessageFileUploadTask,
) {
    /**
     * If non-uploadable message, convert immediately to READY
     * Else, do upload task (concurrent upload on batchId)
     *     If all uploads are successful, convert to READY
     *     Else, throw Exception for convert to FAILED
     */
    suspend operator fun invoke(entry: MessageOutboxEntry) {
        val payload = entry.payload

        require(payload !is SystemMessage)

        try {
            when(payload) {
                is AudioMessage,
                is DocumentMessage,
                is GifMessage,
                is ImageMessage,
                is VideoMessage,
                is VoiceMessage -> messageFileUploadTask(entry)
                else -> {
                    conversationRepository.updateMessageOutboxStateTo(entry.batchId, MessageOutboxStatus.READY)
                    return
                }
            }

            if(conversationRepository.isBatchUploadCompleteSuccessfully(entry.batchId)) {
                val updatedEntry = ConvertMessagePayloadToOriginalTask(conversationRepository).invoke(entry)
                    .copy(status = MessageOutboxStatus.READY)

                conversationRepository.upsertMessageOutboxEntry(updatedEntry)
            } else {
                conversationRepository.updateMessageOutboxStateTo(entry.batchId, MessageOutboxStatus.FAILED)
            }
        } catch (_: Exception) {
            currentCoroutineContext().ensureActive()
            conversationRepository.updateMessageOutboxStateTo(entry.batchId, MessageOutboxStatus.FAILED)
        }
    }
}

private class ConvertMessagePayloadToOriginalTask(
    private val conversationRepository: ConversationRepository
) {
    suspend fun invoke(entry: MessageOutboxEntry): MessageOutboxEntry {
        val newPayload = when(val payload = entry.payload) {
            is AudioMessage -> payload.copy(
                media = payload.media.copy(url = extractUrl(batchId = entry.batchId, uploadId = payload.media.id.id))
            )
            is DocumentMessage -> payload.copy(
                media = payload.media.copy(url = extractUrl(batchId = entry.batchId, uploadId = payload.media.id.id))
            )
            is GifMessage -> payload.copy(
                media = payload.media.copy(url = extractUrl(batchId = entry.batchId, uploadId = payload.media.id.id))
            )
            is ImageMessage -> payload.copy(
                medias = payload.medias.map {
                    it.copy(url = extractUrl(batchId = entry.batchId, uploadId = it.id.id))
                }
            )
            is VideoMessage -> payload.copy(
                media = payload.media.copy(url = extractUrl(batchId = entry.batchId, uploadId = payload.media.id.id))
            )
            is VoiceMessage -> payload.copy(
                media = payload.media.copy(url = extractUrl(batchId = entry.batchId, uploadId = payload.media.id.id))
            )
            else -> payload
        }

        val newOutboxEntry = entry.copy(payload = newPayload)

        return conversationRepository.upsertMessageOutboxEntry(newOutboxEntry)
    }

    suspend fun extractUrl(batchId: String, uploadId: String): String {
        val uploadEntry = conversationRepository.findMessageUploadEntry(batchId, uploadId)
        val completedStatus = uploadEntry.status
        require(completedStatus is MessageUploadStatus.Completed)
        return completedStatus.publicUrl
    }
}