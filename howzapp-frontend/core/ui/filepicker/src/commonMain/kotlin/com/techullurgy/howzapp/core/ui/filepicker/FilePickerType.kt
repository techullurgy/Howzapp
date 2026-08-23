package com.techullurgy.howzapp.core.ui.filepicker

sealed interface FilePickerType {
    data object All : FilePickerType
    data object Image : FilePickerType
    data object Video : FilePickerType
    data object Audio : FilePickerType
    data class Custom(val extensions: List<String>) : FilePickerType
}