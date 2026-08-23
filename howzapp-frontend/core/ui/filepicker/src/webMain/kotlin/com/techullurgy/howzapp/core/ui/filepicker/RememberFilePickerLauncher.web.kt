package com.techullurgy.howzapp.core.ui.filepicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.techullurgy.howzapp.core.files.BrowserFile
import com.techullurgy.howzapp.core.files.PlatformFile
import kotlinx.coroutines.launch
import web.dom.document
import web.events.EventHandler
import web.html.HTMLInputElement
import web.html.InputType
import web.html.file

@Composable
actual fun rememberFilePickerLauncher(
    type: FilePickerType,
    onFilePicked: (PlatformFile?) -> Unit
): FilePickerLauncher {
    val scope = rememberCoroutineScope()

    return remember(type) {
        FilePickerLauncher {
            scope.launch {
                val selectedFiles = WebFilePicker.openPicker(type = type, multiple = false)
                val platformFile = selectedFiles.firstOrNull()?.let { BrowserFile(it) }
                onFilePicked(platformFile)
            }
        }
    }
}