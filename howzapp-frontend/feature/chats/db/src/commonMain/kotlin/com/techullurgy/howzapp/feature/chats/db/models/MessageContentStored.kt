package com.techullurgy.howzapp.feature.chats.db.models

import kotlinx.serialization.Serializable

@Serializable
sealed interface MessageContentStored {
    @Serializable
    data class AudioMessageStored(
        val media: MediaStored,
        val title: String?,
        val artist: String?,
        val durationSeconds: Int
    ) : MessageContentStored

    @Serializable
    data class CallMessageStored(
        val callType: CallTypeStored,
        val durationSeconds: Int
    ) : MessageContentStored

    @Serializable
    data class ContactMessageStored(
        val contact: ContactCardStored
    ) : MessageContentStored

    @Serializable
    data class DocumentMessageStored(
        val media: String,
        val fileName: String,
        val fileSize: Long,
        val mimeType: String
    ) : MessageContentStored

    @Serializable
    data class GifMessageStored(
        val media: MediaStored
    ) : MessageContentStored

    @Serializable
    data class ImageMessageStored(
        val media: MediaStored,
        val caption: String?
    ): MessageContentStored

    @Serializable
    data class LocationMessageStored(
        val latitude: Double,
        val longitude: Double,
        val address: String?,
        val liveDurationSeconds: Int?
    ) : MessageContentStored

    @Serializable
    data class PollMessageStored(
        val question: String,
        val options: List<PollOptionStored>,
        val multipleAnswers: Boolean
    ) : MessageContentStored

    @Serializable
    data class StickerMessageStored(
        val media: MediaStored,
        val animated: Boolean
    ) : MessageContentStored

    @Serializable
    data class SystemMessageStored(
        val event: SystemEventStored
    ) : MessageContentStored

    @Serializable
    data class TextMessageStored(
        val text: String
    ): MessageContentStored

    @Serializable
    data class VideoMessageStored(
        val media: MediaStored,
        val caption: String?,
        val durationSeconds: Int
    ): MessageContentStored

    @Serializable
    data class VoiceMessageStored(
        val media: MediaStored,
        val durationSeconds: Int,
        val waveForm: List<Int>,
    ): MessageContentStored
}

@Serializable
data class MediaStored(
    val id: String,
    val url: String,
    val thumbnailUrl: String?,
    val mimeType: String,
    val sizeBytes: Long,
    val width: Int?,
    val height: Int?
)

enum class CallTypeStored {
    AUDIO,
    VIDEO,
    MISSED_AUDIO,
    MISSED_VIDEO
}

@Serializable
data class ContactCardStored(
    val name: String,
    val phoneNumber: String,
    val organization: String?
)

@Serializable
data class PollOptionStored(
    val id: String,
    val title: String,
    val voters: List<String>
)

@Serializable
sealed interface SystemEventStored {
    @Serializable
    data class UserJoinedStored(
        val userId: String
    ) : SystemEventStored

    @Serializable
    data class UserLeftStored(
        val userId: String
    ) : SystemEventStored

    @Serializable
    data class GroupNameChangedStored(
        val oldName: String,
        val newName: String
    ) : SystemEventStored

    @Serializable
    data class GroupIconChangedStored(
        val iconUrl: String
    ) : SystemEventStored

    @Serializable
    data class AdminPromotedStored(
        val userId: String
    ) : SystemEventStored

    @Serializable
    data class AdminRemovedStored(
        val userId: String
    ) : SystemEventStored

    @Serializable
    data object EndToEndEncryptionEnabledStored : SystemEventStored
}