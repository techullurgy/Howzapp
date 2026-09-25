package com.techullurgy.howzapp.core.network.http.fileupload

import com.techullurgy.howzapp.core.files.PlatformFile
import com.techullurgy.howzapp.core.network.http.core.NetworkRequestParams
import kotlinx.coroutines.flow.Flow

interface FileUploadClient {
    fun uploadFile(
        params: NetworkRequestParams.WithoutBody,
        file: PlatformFile,
        chunkSize: Int = 4 * 1024 * 1024 // 4 MB
    ): Flow<FileUploadStatus>
}