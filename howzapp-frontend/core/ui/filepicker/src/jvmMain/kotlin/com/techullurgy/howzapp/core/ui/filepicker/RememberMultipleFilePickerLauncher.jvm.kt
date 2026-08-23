package com.techullurgy.howzapp.core.ui.filepicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.techullurgy.howzapp.core.files.JvmFile
import com.techullurgy.howzapp.core.files.PlatformFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
actual fun rememberMultipleFilePickerLauncher(
    type: FilePickerType,
    onFilesPicked: (List<PlatformFile>) -> Unit
): FilePickerLauncher {
    val scope = rememberCoroutineScope()

    return remember(type) {
        FilePickerLauncher {
            scope.launch {
                val selectedFiles = withContext(Dispatchers.IO) {
                    JvmFilePicker.openMultiplePicker(type)
                }
                onFilesPicked(selectedFiles.map { JvmFile(it) })
            }
        }
    }
}