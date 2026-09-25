package com.techullurgy.howzapp.core.files

import android.content.Context
import androidx.startup.Initializer

class FileContextInitializer: Initializer<Unit> {
    override fun create(context: Context) {
        AndroidFileContext.init(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> = emptyList()
}