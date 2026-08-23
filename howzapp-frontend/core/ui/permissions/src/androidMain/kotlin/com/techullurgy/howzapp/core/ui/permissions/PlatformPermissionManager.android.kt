package com.techullurgy.howzapp.core.ui.permissions

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

actual class PlatformPermissionManager(
    private val activity: ComponentActivity
) {
    private lateinit var permissionLauncherContinuationHolder: PermissionLauncherContinuationHolder
    private val launcher = activity.registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        val status = if(granted) {
            PermissionStatus.Granted
        } else if (
            ActivityCompat.shouldShowRequestPermissionRationale(
                activity, permissionLauncherContinuationHolder.permission.toAndroidPermissionString()
            )
        ) {
            PermissionStatus.PermanentlyDenied
        } else {
            PermissionStatus.Denied
        }
        permissionLauncherContinuationHolder.continuation.resume(status)
    }

    actual suspend fun check(permission: AppPermission): PermissionStatus {
        if(ActivityCompat.shouldShowRequestPermissionRationale(activity, permission.toAndroidPermissionString())) {
            return PermissionStatus.PermanentlyDenied
        }

        return if(
            ContextCompat.checkSelfPermission(
                activity,
                permission.toAndroidPermissionString()
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            PermissionStatus.Granted
        } else {
            PermissionStatus.Denied
        }
    }

    actual suspend fun request(permission: AppPermission): PermissionStatus = suspendCancellableCoroutine {
        permissionLauncherContinuationHolder = PermissionLauncherContinuationHolder(
            continuation = it,
            permission = permission
        )
        launcher.launch(permission.toAndroidPermissionString())
    }

    private data class PermissionLauncherContinuationHolder(
        val permission: AppPermission,
        val continuation: CancellableContinuation<PermissionStatus>
    )
}

private fun AppPermission.toAndroidPermissionString(): String {
    return when(this) {
        AppPermission.Camera -> Manifest.permission.CAMERA
        AppPermission.RecordAudio -> Manifest.permission.RECORD_AUDIO
        AppPermission.Storage -> Manifest.permission.READ_EXTERNAL_STORAGE
    }
}