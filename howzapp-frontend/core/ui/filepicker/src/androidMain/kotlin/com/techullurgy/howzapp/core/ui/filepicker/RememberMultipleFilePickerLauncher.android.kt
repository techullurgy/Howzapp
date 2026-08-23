package com.techullurgy.howzapp.core.ui.filepicker

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.techullurgy.howzapp.core.files.AndroidFile
import com.techullurgy.howzapp.core.files.PlatformFile

@Composable
actual fun rememberMultipleFilePickerLauncher(
    type: FilePickerType,
    onFilesPicked: (List<PlatformFile>) -> Unit
): FilePickerLauncher {
    val context = LocalContext.current
    val contentResolver = context.applicationContext.contentResolver

    return rememberAndroidLauncher(
        type = type,
        isMultiple = false,
        onFilesPicked = {
            if(it.isEmpty()) {
                onFilesPicked(emptyList())
            } else {
                onFilesPicked(
                    it.map { uri ->
                        AndroidFile.Shared(
                            uri = uri,
                            contentResolver = contentResolver
                        )
                    }
                )
            }
        }
    )
}