package com.techullurgy.howzapp.media.controllers

import com.techullurgy.howzapp.media.dto.ConfirmUploadRequest
import com.techullurgy.howzapp.media.dto.ConfirmUploadResponse
import com.techullurgy.howzapp.media.dto.PresignedUrlRequest
import com.techullurgy.howzapp.media.dto.PresignedUrlResponse
import com.techullurgy.howzapp.media.dto.ValidateMediaRequest
import com.techullurgy.howzapp.media.dto.ValidateMediaResponse
import com.techullurgy.howzapp.media.services.MediaService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/api/v1/media")
class MediaController(
    private val mediaService: MediaService
) {

    /**
     * Client requests presigned URL for direct S3 upload
     */
    @PostMapping("/upload-url")
    suspend fun getUploadUrl(
        principal: Principal,
        @RequestBody request: PresignedUrlRequest
    ): ResponseEntity<PresignedUrlResponse> {
        val response = mediaService.createPresignedUrl(principal.name, request)
        return ResponseEntity.ok(response)
    }

    /**
     * Client confirms upload complete and receives unique_key
     */
    @PostMapping("/confirm")
    suspend fun confirmUpload(
        principal: Principal,
        @RequestBody request: ConfirmUploadRequest
    ): ResponseEntity<ConfirmUploadResponse> {
        val response = mediaService.confirmUpload(principal.name, request)
        return ResponseEntity.ok(response)
    }

    /**
     * Internal endpoint for inter-service validation (Conversation, Status, Profile)
     */
    @PostMapping("/validate")
    suspend fun validateMedia(
        @RequestBody request: ValidateMediaRequest
    ): ResponseEntity<ValidateMediaResponse> {
        val response = mediaService.validateMediaContext(request)
        return ResponseEntity.ok(response)
    }
}