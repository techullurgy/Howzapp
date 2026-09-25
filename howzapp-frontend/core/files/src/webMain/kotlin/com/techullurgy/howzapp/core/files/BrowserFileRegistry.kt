package com.techullurgy.howzapp.core.files

import web.file.File
import kotlin.random.Random

internal object BrowserFileRegistry {
    private val files = mutableMapOf<String, File>()

    fun register(file: File): String {
        // Generate an internal pseudo-URI or UUID
        val id = "browser-file://${file.name}_${file.size}_${file.lastModified}_${Random.nextLong()}"
        files[id] = file
        return id
    }

    fun get(identifier: String): File? = files[identifier]

    fun unregister(identifier: String) {
        files.remove(identifier)
    }
}