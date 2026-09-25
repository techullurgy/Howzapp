package com.techullurgy.howzapp.core.network.fileupload

import com.techullurgy.howzapp.core.files.PlatformFile
import com.techullurgy.howzapp.core.network.http.NetworkRequestParams

data class UploadFileRequest(
    val uploadId: UploadId,
    val file: PlatformFile,
    val initParams: NetworkRequestParams,
    val commitParams: NetworkRequestParams,
)
