package com.techullurgy.howzapp.core.files

import android.annotation.SuppressLint
import android.net.Uri
import java.io.File

actual object PlatformFileFactory {
    @SuppressLint("UseKtx")
    actual fun create(identifier: String): PlatformFile {
        return when {
            identifier.startsWith("content://") -> {
                AndroidFile.Shared(
                    uri = Uri.parse(identifier),
                    contentResolver = AndroidFileContext.contentResolver
                )
            }
            identifier.startsWith("file://") -> {
                val cleanPath = Uri.parse(identifier).path
                    ?: throw IllegalArgumentException("Invalid file URI: $identifier")
                AndroidFile.Internal(File(cleanPath))
            }
            else -> {
                // Treats bare absolute/relative paths as local internal files
                AndroidFile.Internal(File(identifier))
            }
        }
    }
}