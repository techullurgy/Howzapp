package com.techullurgy.howzapp.core.ui.filepicker

import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.io.FilenameFilter
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

internal object JvmFilePicker {
    fun openSinglePicker(
        type: FilePickerType,
        title: String = "Select File"
    ): File? {
        val files = openPicker(type = type, title = title, multiple = false)
        return files.firstOrNull()
    }

    fun openMultiplePicker(
        type: FilePickerType,
        title: String = "Select Files"
    ): List<File> {
        return openPicker(type = type, title = title, multiple = true)
    }

    private fun openPicker(
        type: FilePickerType,
        title: String,
        multiple: Boolean
    ): List<File> {
        val osName = System.getProperty("os.name").lowercase()

        // macOS and Windows native FileDialog supports multi-selection natively
        return if (osName.contains("mac") || osName.contains("win")) {
            val dialog = FileDialog(null as Frame?, title, FileDialog.LOAD).apply {
                isMultipleMode = multiple

                // Configure extension filter
                type.toFilenameFilter()?.let { filenameFilter = it }
            }
            dialog.isVisible = true

            dialog.files.toList()
        } else {
            // Linux / Swing fallback using JFileChooser
            val chooser = JFileChooser().apply {
                dialogTitle = title
                isMultiSelectionEnabled = multiple

                type.toFileNameExtensionFilter()?.let { fileFilter = it }
            }
            val result = chooser.showOpenDialog(null)
            if (result == JFileChooser.APPROVE_OPTION) {
                if (multiple) chooser.selectedFiles.toList() else listOfNotNull(chooser.selectedFile)
            } else {
                emptyList()
            }
        }
    }
}

private fun FilePickerType.toFilenameFilter(): FilenameFilter? = when (this) {
    FilePickerType.All -> null
    FilePickerType.Image -> FilenameFilter { _, name ->
        val ext = name.substringAfterLast('.', "").lowercase()
        ext in listOf("jpg", "jpeg", "png", "webp", "gif", "bmp")
    }
    FilePickerType.Video -> FilenameFilter { _, name ->
        val ext = name.substringAfterLast('.', "").lowercase()
        ext in listOf("mp4", "mkv", "mov", "avi", "webm")
    }
    FilePickerType.Audio -> FilenameFilter { _, name ->
        val ext = name.substringAfterLast('.', "").lowercase()
        ext in listOf("mp3", "wav", "ogg", "flac", "aac", "m4a")
    }
    is FilePickerType.Custom -> FilenameFilter { _, name ->
        val ext = name.substringAfterLast('.', "").lowercase()
        ext in extensions.map { it.lowercase() }
    }
}

private fun FilePickerType.toFileNameExtensionFilter(): FileNameExtensionFilter? = when (this) {
    FilePickerType.All -> null
    FilePickerType.Image -> FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "webp", "gif", "bmp")
    FilePickerType.Video -> FileNameExtensionFilter("Videos", "mp4", "mkv", "mov", "avi", "webm")
    FilePickerType.Audio -> FileNameExtensionFilter("Audio", "mp3", "wav", "ogg", "flac", "aac", "m4a")
    is FilePickerType.Custom -> FileNameExtensionFilter("Custom Files", *extensions.toTypedArray())
}