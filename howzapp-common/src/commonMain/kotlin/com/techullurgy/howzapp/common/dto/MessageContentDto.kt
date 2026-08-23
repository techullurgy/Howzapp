package com.techullurgy.howzapp.common.dto

import kotlinx.serialization.Serializable

@Serializable
sealed interface MessageContentDto {
    @Serializable
    data class AudioMessageDto(
        val media: MediaDto,
        val title: String?,
        val artist: String?,
        val durationSeconds: Int
    ) : MessageContentDto

    @Serializable
    data class CallMessageDto(
        val callType: CallTypeDto,
        val durationSeconds: Int
    ) : MessageContentDto

    @Serializable
    data class ContactMessageDto(
        val contact: ContactCardDto
    ) : MessageContentDto

    @Serializable
    data class DocumentMessageDto(
        val media: String,
        val fileName: String,
        val fileSize: Long,
        val mimeType: String
    ) : MessageContentDto

    @Serializable
    data class GifMessageDto(
        val media: MediaDto
    ) : MessageContentDto

    @Serializable
    data class ImageMessageDto(
        val media: MediaDto,
        val caption: String?
    ): MessageContentDto

    @Serializable
    data class LocationMessageDto(
        val latitude: Double,
        val longitude: Double,
        val address: String?,
        val liveDurationSeconds: Int?
    ) : MessageContentDto

    @Serializable
    data class PollMessageDto(
        val question: String,
        val options: List<PollOptionDto>,
        val multipleAnswers: Boolean
    ) : MessageContentDto

    @Serializable
    data class StickerMessageDto(
        val media: MediaDto,
        val animated: Boolean
    ) : MessageContentDto

    @Serializable
    data class SystemMessageDto(
        val event: SystemEventDto
    ) : MessageContentDto

    @Serializable
    data class TextMessageDto(
        val text: String
    ): MessageContentDto

    @Serializable
    data class VideoMessageDto(
        val media: MediaDto,
        val caption: String?,
        val durationSeconds: Int
    ): MessageContentDto

    @Serializable
    data class VoiceMessageDto(
        val media: MediaDto,
        val durationSeconds: Int,
        val waveForm: List<Int>,
    ): MessageContentDto
}

@Serializable
data class MediaDto(
    val id: String,
    val url: String,
    val thumbnailUrl: String?,
    val mimeType: String,
    val sizeBytes: Long,
    val width: Int?,
    val height: Int?
)

enum class CallTypeDto {
    AUDIO,
    VIDEO,
    MISSED_AUDIO,
    MISSED_VIDEO
}

@Serializable
data class ContactCardDto(
    val name: String,
    val phoneNumber: String,
    val organization: String?
)

@Serializable
data class PollOptionDto(
    val id: String,
    val title: String,
    val voters: List<String>
)

@Serializable
sealed interface SystemEventDto {
    @Serializable
    data class UserJoinedDto(
        val userId: String
    ) : SystemEventDto

    @Serializable
    data class UserLeftDto(
        val userId: String
    ) : SystemEventDto

    @Serializable
    data class GroupNameChangedDto(
        val oldName: String,
        val newName: String
    ) : SystemEventDto

    @Serializable
    data class GroupIconChangedDto(
        val iconUrl: String
    ) : SystemEventDto

    @Serializable
    data class AdminPromotedDto(
        val userId: String
    ) : SystemEventDto

    @Serializable
    data class AdminRemovedDto(
        val userId: String
    ) : SystemEventDto

    @Serializable
    data object EndToEndEncryptionEnabledDto : SystemEventDto
}