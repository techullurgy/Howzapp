package com.techullurgy.howzapp.feature.common.domain.impl.services

import com.techullurgy.howzapp.core.domain.FileUploadProgress
import com.techullurgy.howzapp.core.domain.UploadId
import com.techullurgy.howzapp.core.files.PlatformFile
import com.techullurgy.howzapp.core.network.http.core.NetworkRequestParams
import com.techullurgy.howzapp.core.network.http.fileupload.FileUploadClient
import com.techullurgy.howzapp.feature.common.domain.api.repos.FileUploadRepository
import com.techullurgy.howzapp.feature.common.domain.api.services.FileUploadManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal class FileUploadManagerImpl(
    private val fileUploadRepository: FileUploadRepository,
    private val fileUploadClient: FileUploadClient
): FileUploadManager {
    override val fileUploadProgresses: StateFlow<Map<UploadId, FileUploadProgress>> field = MutableStateFlow(emptyMap())

    override suspend fun uploadProfilePicture(uploadId: UploadId): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun uploadFileMessages(uploadIds: List<UploadId>): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun uploadStatusUpdateFile(uploadIds: List<UploadId>): Boolean {
        TODO("Not yet implemented")
    }

    private suspend fun uploadFile(
        uploadIds: List<UploadId>,
    ) {
        lateinit var file: PlatformFile
        fileUploadClient.uploadFile(
            params = NetworkRequestParams.WithoutBody(""),
            file = file
        ).collect {

        }
    }
}