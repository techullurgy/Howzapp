package com.techullurgy.howzapp.core.network.fileupload

import kotlinx.coroutines.flow.Flow


interface FileUploadClient {
    suspend fun uploadFiles(
        requests: List<UploadFileRequest>,
        cancellationSignal: Flow<Unit>,
        onUpdate: suspend (UploadFileUpdate) -> Unit
    )
}