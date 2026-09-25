package com.techullurgy.howzapp.feature.chats.db.models

import kotlinx.serialization.Serializable

@Serializable
sealed interface MessageUploadStatusStored {
    @Serializable
    data object Initiated: MessageUploadStatusStored

    @Serializable
    data class Success(val publicUrl: String): MessageUploadStatusStored

    @Serializable
    data class Uploading(val progress: Float): MessageUploadStatusStored

    @Serializable
    data object Cancelled: MessageUploadStatusStored

    @Serializable
    data object Failed: MessageUploadStatusStored

}