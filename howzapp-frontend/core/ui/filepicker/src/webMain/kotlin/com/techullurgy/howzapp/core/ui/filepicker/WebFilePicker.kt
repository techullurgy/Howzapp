package com.techullurgy.howzapp.core.ui.filepicker

import kotlinx.coroutines.suspendCancellableCoroutine
import web.dom.document
import web.events.EventHandler
import web.file.File
import web.html.HTMLInputElement
import web.html.InputType
import web.html.file
import kotlin.coroutines.resume

internal object WebFilePicker {
    suspend fun openPicker(
        type: FilePickerType,
        multiple: Boolean
    ): List<File> = suspendCancellableCoroutine { continuation ->
        val input = (document.createElement("input") as HTMLInputElement).apply {
            this.type = InputType.file
            this.multiple = multiple
            this.accept = type.toAcceptString()
            this.style.display = "none"
        }

        document.body.appendChild(input)

        input.onchange = EventHandler {
            val fileList = input.files
            val result = mutableListOf<File>()
            if (fileList != null) {
                for (i in 0 until fileList.length) {
                    fileList.item(i)?.let { result.add(it) }
                }
            }
            document.body.removeChild(input)
            if (continuation.isActive) {
                continuation.resume(result)
            }
        }

        // Cancel support when dialog is closed without selection
        input.oncancel = EventHandler {
            document.body.removeChild(input)
            if (continuation.isActive) {
                continuation.resume(emptyList())
            }
        }

        input.click()

        continuation.invokeOnCancellation {
            if (document.body.contains(input) == true) {
                document.body.removeChild(input)
            }
        }
    }
}

private fun FilePickerType.toAcceptString(): String = when (this) {
    FilePickerType.All -> "*/*"
    FilePickerType.Image -> "image/*"
    FilePickerType.Video -> "video/*"
    FilePickerType.Audio -> "audio/*"
    is FilePickerType.Custom -> extensions.joinToString(",") { ext ->
        val clean = ext.trim().lowercase()
        if (clean.startsWith(".")) clean else ".$clean"
    }
}