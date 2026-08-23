package com.techullurgy.howzapp.core.ui.filepicker

class FilePickerLauncher(
    private val onLaunch: () -> Unit
) {
    fun launch() = onLaunch()
}