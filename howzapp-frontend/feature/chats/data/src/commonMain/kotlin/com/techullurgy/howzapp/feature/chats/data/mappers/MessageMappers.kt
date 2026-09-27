package com.techullurgy.howzapp.feature.chats.data.mappers

import com.techullurgy.howzapp.common.dto.CallTypeDto
import com.techullurgy.howzapp.common.dto.MediaDto
import com.techullurgy.howzapp.common.dto.MessageContentDto
import com.techullurgy.howzapp.common.dto.MessageDeliveryStatusDto
import com.techullurgy.howzapp.common.dto.MessageDto
import com.techullurgy.howzapp.common.dto.MessageReactionDto
import com.techullurgy.howzapp.common.dto.SystemEventDto
import com.techullurgy.howzapp.core.domain.UploadId
import com.techullurgy.howzapp.feature.chats.db.entities.ConversationMessageEntity
import com.techullurgy.howzapp.feature.chats.db.models.CallTypeStored
import com.techullurgy.howzapp.feature.chats.db.models.ContactCardStored
import com.techullurgy.howzapp.feature.chats.db.models.MediaStored
import com.techullurgy.howzapp.feature.chats.db.models.MessageContentStored
import com.techullurgy.howzapp.feature.chats.db.models.MessageDeliveryStatusStored
import com.techullurgy.howzapp.feature.chats.db.models.MessageReactionStored
import com.techullurgy.howzapp.feature.chats.db.models.MessageReactionsStored
import com.techullurgy.howzapp.feature.chats.db.models.MessageUploadStatusStored
import com.techullurgy.howzapp.feature.chats.db.models.PollOptionStored
import com.techullurgy.howzapp.feature.chats.db.models.SystemEventStored
import com.techullurgy.howzapp.feature.chats.db.relations.OutboxMessageRelation
import com.techullurgy.howzapp.feature.chats.domain.api.models.ConversationId
import com.techullurgy.howzapp.feature.chats.domain.api.models.ConversationMessage
import com.techullurgy.howzapp.feature.chats.domain.api.models.ConversationMessageId
import com.techullurgy.howzapp.feature.chats.domain.api.models.MessageDeliveryStatus
import com.techullurgy.howzapp.feature.chats.domain.api.models.MessageUploadStatus
import com.techullurgy.howzapp.feature.chats.domain.api.models.OutboxMessage
import com.techullurgy.howzapp.feature.chats.domain.api.models.OutboxMessageContent
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.AudioMessage
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.CallMessage
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.CallType
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.ContactCard
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.ContactMessage
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.DocumentMessage
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.GifMessage
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.ImageMessage
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.LocationMessage
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.Media
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.MediaId
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.MessageContent
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.MessageReaction
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.PollMessage
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.PollOption
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.StickerMessage
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.SystemEvent
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.SystemMessage
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.TextMessage
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.VideoMessage
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.VoiceMessage
import com.techullurgy.howzapp.feature.users.domain.api.models.UserId
import kotlin.time.Clock
import kotlin.time.Instant

internal fun MessageDto.toConversationMessage(): ConversationMessage {
    return ConversationMessage(
        id = ConversationMessageId(messageId),
        conversationId = ConversationId(conversationId),
        seqNo = seqNo,
        senderId = UserId(senderId),
        content = content.toMessageContent(),
        timestamp = Instant.fromEpochMilliseconds(timestamp),
        status = status?.toMessageDeliveryStatus(),
        reactions = reactions.map { it.toMessageReaction() },
        replyTo = replyTo?.let { ConversationMessageId(it) },
        forwarded = forwarded,
        edited = edited,
        starred = starred,
        deleted = deleted,
        isRead = isRead
    )
}

internal fun ConversationMessage.toConversationMessageEntity(): ConversationMessageEntity {
    return ConversationMessageEntity(
        id = id.id,
        conversation = conversationId.id,
        seqNo = seqNo,
        senderId = senderId.id,
        createdAt = timestamp.toEpochMilliseconds(),
        updatedAt = Clock.System.now().toEpochMilliseconds(),
        content = content.toMessageContentStored(),
        status = status?.toMessageDeliveryStatusStored(),
        reactions = reactions.toMessageReactionsStored(),
        replyTo = replyTo?.id,
        forwarded = forwarded,
        edited = edited,
        starred = starred,
        deleted = deleted,
        isRead = isRead
    )
}

internal fun ConversationMessageEntity.toConversationMessage(): ConversationMessage {
    return ConversationMessage(
        id = ConversationMessageId(id),
        conversationId = ConversationId(conversation),
        seqNo = seqNo,
        senderId = UserId(senderId),
        content = content.toMessageContent(),
        timestamp = Instant.fromEpochMilliseconds(createdAt),
        status = status?.toMessageDeliveryStatus(),
        reactions = reactions.toMessageReactions(),
        replyTo = replyTo?.let { ConversationMessageId(it) },
        forwarded = forwarded,
        edited = edited,
        starred = starred,
        deleted = deleted,
        isRead = isRead
    )
}

internal fun MessageContentStored.toMessageContent(): MessageContent {
    return when(this) {
        is MessageContentStored.AudioMessageStored -> AudioMessage(
            media = media.toMedia(),
            title = title,
            artist = artist,
            durationSeconds = durationSeconds
        )
        is MessageContentStored.CallMessageStored -> CallMessage(
            callType = when (callType) {
                CallTypeStored.AUDIO -> CallType.AUDIO
                CallTypeStored.VIDEO -> CallType.VIDEO
                CallTypeStored.MISSED_AUDIO -> CallType.MISSED_AUDIO
                CallTypeStored.MISSED_VIDEO -> CallType.MISSED_VIDEO
            },
            durationSeconds = durationSeconds
        )
        is MessageContentStored.ContactMessageStored -> ContactMessage(
            contact = ContactCard(
                name = contact.name,
                phoneNumber = contact.phoneNumber,
                organization = contact.organization
            )
        )
        is MessageContentStored.DocumentMessageStored -> DocumentMessage(
            media = media.toMedia(),
            fileName = fileName,
            fileSize = fileSize,
            mimeType = mimeType
        )
        is MessageContentStored.GifMessageStored -> GifMessage(media = media.toMedia())
        is MessageContentStored.ImageMessageStored -> ImageMessage(
            medias = medias.map { it.toMedia() },
            caption = caption
        )
        is MessageContentStored.LocationMessageStored -> LocationMessage(
            latitude = latitude,
            longitude = longitude,
            address = address,
            liveDurationSeconds = liveDurationSeconds
        )
        is MessageContentStored.PollMessageStored -> PollMessage(
            question = question,
            options = options.map {
                PollOption(
                    id = it.id,
                    title = it.title,
                    voters = it.voters.map { v -> UserId(v) }
                )
            },
            multipleAnswers = multipleAnswers
        )
        is MessageContentStored.StickerMessageStored -> StickerMessage(
            media = media.toMedia(),
            animated = animated
        )
        is MessageContentStored.SystemMessageStored -> SystemMessage(
            event = when(val event = event) {
                is SystemEventStored.AdminPromotedStored -> SystemEvent.AdminPromoted(UserId(event.userId))
                is SystemEventStored.AdminRemovedStored -> SystemEvent.AdminRemoved(UserId(event.userId))
                is SystemEventStored.GroupIconChangedStored -> SystemEvent.GroupIconChanged(event.iconUrl)
                is SystemEventStored.GroupNameChangedStored -> SystemEvent.GroupNameChanged(
                    oldName = event.oldName,
                    newName = event.newName
                )
                is SystemEventStored.UserJoinedStored -> SystemEvent.UserJoined(UserId(event.userId))
                is SystemEventStored.UserLeftStored -> SystemEvent.UserLeft(UserId(event.userId))
            }
        )
        is MessageContentStored.TextMessageStored -> TextMessage(text)
        is MessageContentStored.VideoMessageStored -> VideoMessage(
            media = media.toMedia(),
            caption = caption,
            durationSeconds = durationSeconds
        )
        is MessageContentStored.VoiceMessageStored -> VoiceMessage(
            media = media.toMedia(),
            durationSeconds = durationSeconds,
            waveForm = waveForm
        )
    }
}

internal fun MediaStored.toMedia(): Media {
    return Media(
        id = MediaId(id),
        url = url,
        thumbnailUrl = thumbnailUrl,
        mimeType = mimeType,
        sizeBytes = sizeBytes,
        width = width,
        height = height
    )
}

internal fun MediaDto.toMedia(): Media {
    return Media(
        id = MediaId(id),
        url = url,
        thumbnailUrl = thumbnailUrl,
        mimeType = mimeType,
        sizeBytes = sizeBytes,
        width = width,
        height = height
    )
}

internal fun Media.toMediaStored(): MediaStored {
    return MediaStored(
        id = id.id,
        url = url,
        thumbnailUrl = thumbnailUrl,
        mimeType = mimeType,
        sizeBytes = sizeBytes,
        width = width,
        height = height
    )
}

internal fun MessageDeliveryStatusStored.toMessageDeliveryStatus(): MessageDeliveryStatus {
    return when(this) {
        MessageDeliveryStatusStored.SENT -> MessageDeliveryStatus.SENT
        MessageDeliveryStatusStored.DELIVERED -> MessageDeliveryStatus.DELIVERED
        MessageDeliveryStatusStored.READ -> MessageDeliveryStatus.READ
    }
}
internal fun MessageReactionsStored.toMessageReactions(): List<MessageReaction> {
    return reactions.map {
        MessageReaction(
            emoji = it.emoji,
            userId = UserId(it.userId),
            timestamp = it.timestamp
        )
    }
}

internal fun MessageContent.toMessageContentStored(): MessageContentStored {
    return when(this) {
        is AudioMessage -> MessageContentStored.AudioMessageStored(
            media = media.toMediaStored(),
            title = title,
            artist = artist,
            durationSeconds = durationSeconds
        )
        is CallMessage -> MessageContentStored.CallMessageStored(
            callType = when (callType) {
                CallType.AUDIO -> CallTypeStored.AUDIO
                CallType.VIDEO -> CallTypeStored.VIDEO
                CallType.MISSED_AUDIO -> CallTypeStored.MISSED_AUDIO
                CallType.MISSED_VIDEO -> CallTypeStored.MISSED_VIDEO
            },
            durationSeconds = durationSeconds
        )
        is ContactMessage -> MessageContentStored.ContactMessageStored(
            contact = ContactCardStored(
                name = contact.name,
                phoneNumber = contact.phoneNumber,
                organization = contact.organization
            )
        )
        is DocumentMessage -> MessageContentStored.DocumentMessageStored(
            media = media.toMediaStored(),
            fileName = fileName,
            fileSize = fileSize,
            mimeType = mimeType
        )
        is GifMessage -> MessageContentStored.GifMessageStored(media = media.toMediaStored())
        is ImageMessage -> MessageContentStored.ImageMessageStored(
            medias = medias.map { it.toMediaStored() },
            caption = caption
        )
        is LocationMessage -> MessageContentStored.LocationMessageStored(
            latitude = latitude,
            longitude = longitude,
            address = address,
            liveDurationSeconds = liveDurationSeconds
        )
        is PollMessage -> MessageContentStored.PollMessageStored(
            question = question,
            options = options.map {
                PollOptionStored(
                    id = it.id,
                    title = it.title,
                    voters = it.voters.map { v -> v.id }
                )
            },
            multipleAnswers = multipleAnswers
        )
        is StickerMessage -> MessageContentStored.StickerMessageStored(
            media = media.toMediaStored(),
            animated = animated
        )
        is SystemMessage -> MessageContentStored.SystemMessageStored(
            event = when(val event = event) {
                is SystemEvent.AdminPromoted -> SystemEventStored.AdminPromotedStored(event.userId.id)
                is SystemEvent.AdminRemoved -> SystemEventStored.AdminRemovedStored(event.userId.id)
                is SystemEvent.GroupIconChanged -> SystemEventStored.GroupIconChangedStored(event.iconUrl)
                is SystemEvent.GroupNameChanged -> SystemEventStored.GroupNameChangedStored(
                    oldName = event.oldName,
                    newName = event.newName
                )
                is SystemEvent.UserJoined -> SystemEventStored.UserJoinedStored(event.userId.id)
                is SystemEvent.UserLeft -> SystemEventStored.UserLeftStored(event.userId.id)
            }
        )
        is TextMessage -> MessageContentStored.TextMessageStored(text)
        is VideoMessage -> MessageContentStored.VideoMessageStored(
            media = media.toMediaStored(),
            caption = caption,
            durationSeconds = durationSeconds
        )
        is VoiceMessage -> MessageContentStored.VoiceMessageStored(
            media = media.toMediaStored(),
            durationSeconds = durationSeconds,
            waveForm = waveForm
        )
    }
}

internal fun MessageDeliveryStatus.toMessageDeliveryStatusStored(): MessageDeliveryStatusStored {
    return when(this) {
        MessageDeliveryStatus.SENT -> MessageDeliveryStatusStored.SENT
        MessageDeliveryStatus.DELIVERED -> MessageDeliveryStatusStored.DELIVERED
        MessageDeliveryStatus.READ -> MessageDeliveryStatusStored.READ
    }
}

internal fun List<MessageReaction>.toMessageReactionsStored(): MessageReactionsStored {
    return MessageReactionsStored(
        reactions = map {
            MessageReactionStored(
                emoji = it.emoji,
                userId = it.userId.id,
                timestamp = it.timestamp
            )
        }
    )
}

internal fun MessageContentDto.toMessageContent(): MessageContent {
    return when(this) {
        is MessageContentDto.AudioMessageDto -> AudioMessage(
            media = media.toMedia(),
            title = title,
            artist = artist,
            durationSeconds = durationSeconds
        )
        is MessageContentDto.CallMessageDto -> CallMessage(
            callType = when (callType) {
                CallTypeDto.AUDIO -> CallType.AUDIO
                CallTypeDto.VIDEO -> CallType.VIDEO
                CallTypeDto.MISSED_AUDIO -> CallType.MISSED_AUDIO
                CallTypeDto.MISSED_VIDEO -> CallType.MISSED_VIDEO
            },
            durationSeconds = durationSeconds
        )
        is MessageContentDto.ContactMessageDto -> ContactMessage(
            contact = ContactCard(
                name = contact.name,
                phoneNumber = contact.phoneNumber,
                organization = contact.organization
            )
        )
        is MessageContentDto.DocumentMessageDto -> DocumentMessage(
            media = media.toMedia(),
            fileName = fileName,
            fileSize = fileSize,
            mimeType = mimeType
        )
        is MessageContentDto.GifMessageDto -> GifMessage(media = media.toMedia())
        is MessageContentDto.ImageMessageDto -> ImageMessage(
            medias = medias.map { it.toMedia() },
            caption = caption
        )
        is MessageContentDto.LocationMessageDto -> LocationMessage(
            latitude = latitude,
            longitude = longitude,
            address = address,
            liveDurationSeconds = liveDurationSeconds
        )
        is MessageContentDto.PollMessageDto -> PollMessage(
            question = question,
            options = options.map {
                PollOption(
                    id = it.id,
                    title = it.title,
                    voters = it.voters.map { v -> UserId(v) }
                )
            },
            multipleAnswers = multipleAnswers
        )
        is MessageContentDto.StickerMessageDto -> StickerMessage(
            media = media.toMedia(),
            animated = animated
        )
        is MessageContentDto.SystemMessageDto -> SystemMessage(
            event = when(val event = event) {
                is SystemEventDto.AdminPromotedDto -> SystemEvent.AdminPromoted(UserId(event.userId))
                is SystemEventDto.AdminRemovedDto -> SystemEvent.AdminRemoved(UserId(event.userId))
                is SystemEventDto.GroupIconChangedDto -> SystemEvent.GroupIconChanged(event.iconUrl)
                is SystemEventDto.GroupNameChangedDto -> SystemEvent.GroupNameChanged(
                    oldName = event.oldName,
                    newName = event.newName
                )
                is SystemEventDto.UserJoinedDto -> SystemEvent.UserJoined(UserId(event.userId))
                is SystemEventDto.UserLeftDto -> SystemEvent.UserLeft(UserId(event.userId))
            }
        )
        is MessageContentDto.TextMessageDto -> TextMessage(text)
        is MessageContentDto.VideoMessageDto -> VideoMessage(
            media = media.toMedia(),
            caption = caption,
            durationSeconds = durationSeconds
        )
        is MessageContentDto.VoiceMessageDto -> VoiceMessage(
            media = media.toMedia(),
            durationSeconds = durationSeconds,
            waveForm = waveForm
        )
    }
}

internal fun MessageDeliveryStatusDto.toMessageDeliveryStatus(): MessageDeliveryStatus {
    return when(this) {
        MessageDeliveryStatusDto.SENT -> MessageDeliveryStatus.SENT
        MessageDeliveryStatusDto.DELIVERED -> MessageDeliveryStatus.DELIVERED
        MessageDeliveryStatusDto.READ -> MessageDeliveryStatus.READ
    }
}

internal fun MessageReactionDto.toMessageReaction(): MessageReaction {
    return MessageReaction(
        emoji = emoji,
        userId = UserId(userId),
        timestamp = Instant.fromEpochMilliseconds(timestamp)
    )
}

internal fun OutboxMessageRelation.toOutboxMessage(): OutboxMessage = OutboxMessage(
    conversationId = outbox.conversationId,
    batchId = outbox.id,
    timestamp = outbox.timestamp,
    payload = OutboxMessageContent(
        content = outbox.payload.toMessageContent(),
        uploadStatuses = upload.takeIf { it.isNotEmpty() }?.map {
            val status: MessageUploadStatus = when(val st = it.status) {
                MessageUploadStatusStored.Cancelled -> MessageUploadStatus.Cancelled
                MessageUploadStatusStored.Failed -> MessageUploadStatus.Failed
                MessageUploadStatusStored.Initiated -> MessageUploadStatus.Initiated
                is MessageUploadStatusStored.Success -> MessageUploadStatus.Completed(st.publicUrl)
                is MessageUploadStatusStored.Uploading -> MessageUploadStatus.Uploading(st.progress)
            }
            UploadId(it.uploadId) to status
        }
    )
)