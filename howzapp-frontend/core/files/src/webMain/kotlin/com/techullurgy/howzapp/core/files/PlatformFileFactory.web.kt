package com.techullurgy.howzapp.core.files

actual object PlatformFileFactory {
    actual fun create(identifier: String): PlatformFile {
        val file = BrowserFileRegistry.get(identifier)
            ?: throw IllegalArgumentException("Browser file not found or expired for ID: $identifier")
        return BrowserFile(file, identifier)
    }
}