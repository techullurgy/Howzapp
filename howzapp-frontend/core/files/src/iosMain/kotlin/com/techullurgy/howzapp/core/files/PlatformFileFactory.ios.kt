package com.techullurgy.howzapp.core.files

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.NSURLBookmarkResolutionWithSecurityScope
import platform.Foundation.create

actual object PlatformFileFactory {
    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    actual fun create(identifier: String): PlatformFile {
        return when {
            // Case A: File URL or standard path
            identifier.startsWith("file://") -> {
                val url = NSURL.URLWithString(identifier)
                    ?: throw IllegalArgumentException("Invalid URL: $identifier")
                AppleFile(url)
            }
            identifier.startsWith("/") -> {
                val url = NSURL.fileURLWithPath(identifier)
                AppleFile(url)
            }

            // Case B: Security-Scoped Bookmark (Base64)
            else -> {
                val data = NSData.create(base64EncodedString = identifier, options = 0u)
                    ?: throw IllegalArgumentException("Invalid identifier: $identifier")

                val resolvedUrl = NSURL.URLByResolvingBookmarkData(
                    bookmarkData = data,
                    options = NSURLBookmarkResolutionWithSecurityScope,
                    relativeToURL = null,
                    bookmarkDataIsStale = null,
                    error = null
                ) ?: throw IllegalArgumentException("Could not resolve bookmark for identifier")

                AppleFile(resolvedUrl, /*securityScopedBookmarkBase64 = identifier*/)
            }
        }
    }
}