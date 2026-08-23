package com.techullurgy.howzapp.core.ui.filepicker

import androidx.compose.runtime.Composable
import com.techullurgy.howzapp.core.files.PlatformFile

@Composable
expect fun rememberMultipleFilePickerLauncher(
    type: FilePickerType = FilePickerType.All,
    onFilesPicked: (List<PlatformFile>) -> Unit
): FilePickerLauncher