package com.techullurgy.howzapp.core.ui.filepicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.techullurgy.howzapp.core.files.JvmFile
import com.techullurgy.howzapp.core.files.PlatformFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.awt.FileDialog
import java.awt.Frame

@Composable
actual fun rememberFilePickerLauncher(
    type: FilePickerType,
    onFilePicked: (PlatformFile?) -> Unit
): FilePickerLauncher {
    // We need a coroutine scope to launch the blocking dialog off the main UI thread
    val coroutineScope = rememberCoroutineScope()

    return remember {
        FilePickerLauncher(
            onLaunch = {
                // Launch on IO dispatcher so we don't freeze the Compose UI
                coroutineScope.launch(Dispatchers.IO) {

                    // Create a native OS file dialog
                    val dialog =
                        FileDialog(null as Frame?, "Select a File to Upload", FileDialog.LOAD)

                    // dialog.setFilenameFilter { dir, name -> name.endsWith(".jpg") } // Optional: filter types

                    // This call blocks the IO thread until the user picks a file or cancels
                    dialog.isVisible = true

                    // Retrieve the selected file
                    val selectedFile = dialog.files.firstOrNull()

                    if (selectedFile != null) {
                        // Map the java.io.File to our Clean Architecture JvmFile
                        onFilePicked(JvmFile(selectedFile))
                    } else {
                        // User closed the dialog without picking a file
                        onFilePicked(null)
                    }
                }
            }
        )
    }
}