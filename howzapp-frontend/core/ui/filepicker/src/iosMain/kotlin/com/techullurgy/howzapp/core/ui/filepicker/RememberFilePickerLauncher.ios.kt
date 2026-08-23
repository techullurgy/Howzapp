package com.techullurgy.howzapp.core.ui.filepicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.techullurgy.howzapp.core.files.AppleFile
import com.techullurgy.howzapp.core.files.PlatformFile
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIDocumentPickerDelegateProtocol
import platform.UIKit.UIDocumentPickerViewController
import platform.UniformTypeIdentifiers.UTTypeData
import platform.darwin.NSObject

@Composable
actual fun rememberFilePickerLauncher(
    type: FilePickerType,
    onFilePicked: (PlatformFile?) -> Unit
): FilePickerLauncher {
    return rememberIosLauncher(
        type = type,
        singleSelection = true,
        onFilesPicked = {
            if(it.isEmpty()) {
                onFilePicked(null)
            } else {
                val file = it.map { url -> AppleFile(url) }.first()
                onFilePicked(file)
            }
        }
    )
}