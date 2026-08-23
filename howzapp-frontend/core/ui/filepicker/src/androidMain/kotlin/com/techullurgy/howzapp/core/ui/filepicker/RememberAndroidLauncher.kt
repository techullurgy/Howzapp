package com.techullurgy.howzapp.core.ui.filepicker

import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
internal fun rememberAndroidLauncher(
    type: FilePickerType,
    isMultiple: Boolean,
    onFilesPicked: (List<Uri>) -> Unit
): FilePickerLauncher {
    val visualMediaType = remember(type) { type.toVisualMediaType() }

    return if (visualMediaType != null) {
        if(isMultiple) {
            // Use PhotoPicker for Image / Video
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.PickVisualMedia()
            ) { uri: Uri? ->
                onFilesPicked(uri?.let { listOf(it) } ?: emptyList())
            }

            remember(launcher, visualMediaType) {
                FilePickerLauncher {
                    launcher.launch(PickVisualMediaRequest(visualMediaType))
                }
            }
        } else {
            // Use PhotoPicker for Image / Video
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.PickVisualMedia()
            ) { uri: Uri? ->
                onFilesPicked(uri?.let { listOf(it) } ?: emptyList())
            }

            remember(launcher, visualMediaType) {
                FilePickerLauncher {
                    launcher.launch(PickVisualMediaRequest(visualMediaType))
                }
            }
        }

    } else {
        if(isMultiple) {
            // Use OpenDocument for All, Audio, Custom
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.OpenDocument()
            ) { uri: Uri? ->
                onFilesPicked(uri?.let { listOf(it) } ?: emptyList())
            }

            val mimeTypes = remember(type) { type.toMimeTypes() }

            remember(launcher, mimeTypes) {
                FilePickerLauncher {
                    launcher.launch(mimeTypes)
                }
            }
        } else {
            // Use OpenDocument for All, Audio, Custom
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.OpenDocument()
            ) { uri: Uri? ->
                onFilesPicked(uri?.let { listOf(it) } ?: emptyList())
            }

            val mimeTypes = remember(type) { type.toMimeTypes() }

            remember(launcher, mimeTypes) {
                FilePickerLauncher {
                    launcher.launch(mimeTypes)
                }
            }
        }
    }
}


private fun FilePickerType.toMimeTypes(): Array<String> = when (this) {
    FilePickerType.All -> arrayOf("*/*")
    FilePickerType.Image -> arrayOf("image/*")
    FilePickerType.Video -> arrayOf("video/*")
    FilePickerType.Audio -> arrayOf("audio/*")
    is FilePickerType.Custom -> {
        val mimeTypes = extensions.mapNotNull { ext ->
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext.lowercase())
        }.toTypedArray()
        if (mimeTypes.isEmpty()) arrayOf("*/*") else mimeTypes
    }
}

// Helper mapping function for PhotoPicker API
private fun FilePickerType.toVisualMediaType(): ActivityResultContracts.PickVisualMedia.VisualMediaType? = when (this) {
    FilePickerType.Image -> ActivityResultContracts.PickVisualMedia.ImageOnly
    FilePickerType.Video -> ActivityResultContracts.PickVisualMedia.VideoOnly
    else -> null
}