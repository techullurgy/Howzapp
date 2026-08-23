package com.techullurgy.howzapp.core.domain

import kotlin.jvm.JvmInline

@JvmInline
value class FileUploadProgress(val progress: Double) {
    init {
        require(progress in 0.0..1.0) {
            "File Upload Progresses are lies in between 0.0 and 1.0 (inclusive) only"
        }
    }

    fun toPercentage(): Int = (progress * 100.0).toInt()
}