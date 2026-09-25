package com.techullurgy.howzapp.core.files

import android.content.ContentResolver
import android.content.Context

internal object AndroidFileContext {
    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    val contentResolver: ContentResolver
        get() {
            check(::appContext.isInitialized) {
                "AndroidFileContext must be initialized with Context before creating PlatformFile"
            }
            return appContext.contentResolver
        }
}