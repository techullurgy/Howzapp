package com.techullurgy.howzapp.core.files

import java.io.File

actual object PlatformFileFactory {
    actual fun create(identifier: String): PlatformFile {
        return JvmFile(File(identifier))
    }
}