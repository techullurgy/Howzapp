package com.techullurgy.howzapp.core.ui.filepicker

import androidx.compose.runtime.Composable
import com.techullurgy.howzapp.core.files.AppleFile
import com.techullurgy.howzapp.core.files.PlatformFile

@Composable
actual fun rememberMultipleFilePickerLauncher(
    type: FilePickerType,
    onFilesPicked: (List<PlatformFile>) -> Unit
): FilePickerLauncher {
    return rememberIosLauncher(
        type = type,
        singleSelection = false,
        onFilesPicked = {
            val files = it.map { url -> AppleFile(url) }
            onFilesPicked(files)
        }
    )
}