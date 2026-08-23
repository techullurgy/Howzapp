package com.techullurgy.howzapp.core.ui.filepicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.Foundation.NSURL
import platform.PhotosUI.PHPickerConfiguration
import platform.PhotosUI.PHPickerFilter
import platform.PhotosUI.PHPickerResult
import platform.PhotosUI.PHPickerViewController
import platform.PhotosUI.PHPickerViewControllerDelegateProtocol
import platform.UIKit.UIApplication
import platform.UIKit.UIDocumentPickerDelegateProtocol
import platform.UIKit.UIDocumentPickerViewController
import platform.UniformTypeIdentifiers.UTTypeData
import platform.darwin.NSObject

@Composable
internal fun rememberIosLauncher(
    type: FilePickerType,
    singleSelection: Boolean,
    onFilesPicked: (List<NSURL>) -> Unit
): FilePickerLauncher {
    return remember(type, singleSelection) {
        FilePickerLauncher {
            val rootController = UIApplication.sharedApplication.keyWindow?.rootViewController

            if(type is FilePickerType.Image || type is FilePickerType.Video) {
                // Photo Picker Path (PHPickerViewController)
                val photoPicker = PHPickerViewController(
                    configuration = PHPickerConfiguration().apply {
                        selectionLimit = if (singleSelection) 1 else 0 // 0 = unlimited
                        filter = when (type) {
                            FilePickerType.Image -> PHPickerFilter.imagesFilter()
                            FilePickerType.Video -> PHPickerFilter.videosFilter()
                        }
                    }
                )

                val delegate = providePhotoPickerDelegate(onFilesPicked)
                photoPicker.delegate = delegate
                rootController?.presentViewController(photoPicker, animated = true, completion = null)
            } else {
                val documentPicker = UIDocumentPickerViewController(
                    forOpeningContentTypes = listOf(UTTypeData),
                    asCopy = true
                ).apply {
                    allowsMultipleSelection = !singleSelection
                }

                val delegate = provideDocumentPickerDelegate(onFilesPicked)
                documentPicker.delegate = delegate
                rootController?.presentViewController(documentPicker, animated = true, completion = null)
            }
        }
    }
}

private fun providePhotoPickerDelegate(
    onFilesPicked: (List<NSURL>) -> Unit
): PHPickerViewControllerDelegateProtocol {
    val delegate = object : NSObject(), PHPickerViewControllerDelegateProtocol {
        override fun picker(picker: PHPickerViewController, didFinishPicking: List<*>) {
            picker.dismissViewControllerAnimated(true, completion = null)
            val results = didFinishPicking.filterIsInstance<PHPickerResult>()
            if(results.isEmpty()) {
                onFilesPicked(emptyList())
                return
            }

            val urls = mutableSetOf<NSURL>()

            for(result in results) {
                val provider = result.itemProvider
                provider.loadInPlaceFileRepresentationForTypeIdentifier(
                    typeIdentifier = UTTypeData.identifier
                ) { url, _, error ->
                    if (url != null && error == null) {
                        urls.add(url)
                    }
                }
            }

            onFilesPicked(urls.toList())
        }
    }

    return delegate
}

private fun provideDocumentPickerDelegate(
    onFilesPicked: (List<NSURL>) -> Unit
): UIDocumentPickerDelegateProtocol {
    val delegate = object: NSObject(), UIDocumentPickerDelegateProtocol {
        override fun documentPicker(
            controller: UIDocumentPickerViewController,
            didPickDocumentsAtURLs: List<*>
        ) {
            val urls = didPickDocumentsAtURLs.filterIsInstance<NSURL>()
            onFilesPicked(urls)
        }

        override fun documentPickerWasCancelled(controller: UIDocumentPickerViewController) {
            onFilesPicked(emptyList())
        }
    }

    return delegate
}