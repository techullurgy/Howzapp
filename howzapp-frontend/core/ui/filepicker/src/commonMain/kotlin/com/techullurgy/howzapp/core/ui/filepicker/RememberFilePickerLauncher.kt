package com.techullurgy.howzapp.core.ui.filepicker

import androidx.compose.runtime.Composable
import com.techullurgy.howzapp.core.files.PlatformFile

@Composable
expect fun rememberFilePickerLauncher(
    type: FilePickerType = FilePickerType.All,
    onFilePicked: (PlatformFile?) -> Unit
): FilePickerLauncher
