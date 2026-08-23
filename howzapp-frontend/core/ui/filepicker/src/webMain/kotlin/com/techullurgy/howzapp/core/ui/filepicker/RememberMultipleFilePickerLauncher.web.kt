package com.techullurgy.howzapp.core.ui.filepicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.techullurgy.howzapp.core.files.BrowserFile
import com.techullurgy.howzapp.core.files.PlatformFile
import kotlinx.coroutines.launch

@Composable
actual fun rememberMultipleFilePickerLauncher(
    type: FilePickerType,
    onFilesPicked: (List<PlatformFile>) -> Unit
): FilePickerLauncher {
    val scope = rememberCoroutineScope()

    return remember(type) {
        FilePickerLauncher {
            scope.launch {
                val selectedFiles = WebFilePicker.openPicker(type = type, multiple = true)
                onFilesPicked(selectedFiles.map { BrowserFile(it) })
            }
        }
    }
}