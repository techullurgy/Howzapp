package com.techullurgy.howzapp.feature.common.domain.api.services

import com.techullurgy.howzapp.core.domain.FileUploadProgress
import com.techullurgy.howzapp.core.domain.UploadId
import kotlinx.coroutines.flow.StateFlow

/**
 * Responsible to
 * 1) Request signing key of Media Server (S3, GCS, etc.,) from our server
 * 2) Upload to the Media Server
 * 3) Verify / Complete that upload with our server (by updating the server key)
 */
interface FileUploadManager {
    val fileUploadProgresses: StateFlow<Map<UploadId, FileUploadProgress>>
    suspend fun uploadProfilePicture(uploadId: UploadId): Boolean
    suspend fun uploadFileMessages(uploadIds: List<UploadId>): Boolean
    suspend fun uploadStatusUpdateFile(uploadIds: List<UploadId>): Boolean
}