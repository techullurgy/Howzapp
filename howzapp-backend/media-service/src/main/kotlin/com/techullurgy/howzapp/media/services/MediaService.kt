package com.techullurgy.howzapp.media.services

import com.techullurgy.howzapp.media.db.entities.MediaMetadataEntity
import com.techullurgy.howzapp.media.dto.ConfirmUploadRequest
import com.techullurgy.howzapp.media.dto.ConfirmUploadResponse
import com.techullurgy.howzapp.media.dto.MediaPurpose
import com.techullurgy.howzapp.media.dto.MediaType
import com.techullurgy.howzapp.media.dto.PresignedUrlRequest
import com.techullurgy.howzapp.media.dto.PresignedUrlResponse
import com.techullurgy.howzapp.media.dto.ValidateMediaRequest
import com.techullurgy.howzapp.media.dto.ValidateMediaResponse
import com.techullurgy.howzapp.media.repository.MediaCachedRepository
import org.springframework.stereotype.Service
import tools.jackson.databind.ObjectMapper
import tools.jackson.module.kotlin.readValue
import java.util.UUID

@Service
class MediaService(
    private val mediaRepository: MediaCachedRepository,
    private val s3PresignerService: S3PresignerService,
    private val objectMapper: ObjectMapper
) {

    /**
     * Step 1: Generates Presigned S3 URL and Ephemeral Upload Token
     */
    suspend fun createPresignedUrl(ownerUserId: String, request: PresignedUrlRequest): PresignedUrlResponse {
        val fileExtension = getExtensionFromMime(request.mimeType)
        val objectKey = "uploads/${UUID.randomUUID()}.$fileExtension"

        val (uploadUrl, originalUrl) = s3PresignerService.generatePresignedUploadUrl(objectKey, request.mimeType)
        val uploadToken = "upl_tok_${UUID.randomUUID()}"

        // Save upload metadata associated with token
        val tokenData = mapOf(
            "ownerUserId" to ownerUserId,
            "mediaType" to request.fileType.name,
            "purpose" to request.purpose.name,
            "originalUrl" to originalUrl
        )
        mediaRepository.saveUploadToken(uploadToken, objectMapper.writeValueAsString(tokenData))

        return PresignedUrlResponse(
            uploadUrl = uploadUrl,
            originalUrl = originalUrl,
            uploadToken = uploadToken
        )
    }

    /**
     * Step 2: Confirms successful S3 upload and issues permanent unique_key
     */
    suspend fun confirmUpload(ownerUserId: String, request: ConfirmUploadRequest): ConfirmUploadResponse {
        val rawTokenData = mediaRepository.getUploadTokenData(request.uploadToken)
            ?: throw IllegalArgumentException("Invalid or expired upload token")

        val tokenMap: Map<String, String> = objectMapper.readValue(rawTokenData)
        val tokenOwner = tokenMap["ownerUserId"] ?: ""
        val tokenOriginalUrl = tokenMap["originalUrl"] ?: ""
        val purposeStr = tokenMap["purpose"] ?: MediaPurpose.MEDIA_MESSAGE.name

        require(tokenOwner == ownerUserId) { "Upload token ownership mismatch" }
        require(tokenOriginalUrl == request.originalUrl) { "Original URL mismatch" }

        val uniqueKey = "med_key_${UUID.randomUUID().toString().replace("-", "").take(16)}"

        val entity = MediaMetadataEntity(
            uniqueKey = uniqueKey,
            originalUrl = request.originalUrl,
            ownerUserId = ownerUserId,
            mediaType = tokenMap["mediaType"] ?: MediaType.IMAGE.name,
            purpose = purposeStr,
            isClaimed = false
        )

        mediaRepository.saveMediaMetadata(entity)
        mediaRepository.deleteUploadToken(request.uploadToken)

        return ConfirmUploadResponse(
            uniqueKey = uniqueKey,
            originalUrl = request.originalUrl,
            purpose = MediaPurpose.valueOf(purposeStr)
        )
    }

    /**
     * Step 3: Validation handshake invoked by other services (Conversation, Status, Profile)
     */
    suspend fun validateMediaContext(request: ValidateMediaRequest): ValidateMediaResponse {
        val metadata = mediaRepository.findMediaMetadata(request.uniqueKey)
            ?: return ValidateMediaResponse(isValid = false)

        val matches = metadata.originalUrl == request.originalUrl &&
                metadata.purpose == request.purpose.name

        return ValidateMediaResponse(
            isValid = matches,
            ownerUserId = if (matches) metadata.ownerUserId else null
        )
    }

    private fun getExtensionFromMime(mimeType: String): String {
        return when (mimeType.lowercase()) {
            "image/jpeg" -> "jpg"
            "image/png" -> "png"
            "video/mp4" -> "mp4"
            "audio/mpeg" -> "mp3"
            "audio/ogg" -> "ogg"
            "application/pdf" -> "pdf"
            else -> "bin"
        }
    }
}