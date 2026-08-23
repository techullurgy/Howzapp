package com.techullurgy.howzapp.core.ui.permissions

actual class PlatformPermissionManager {
    actual suspend fun check(permission: AppPermission): PermissionStatus {
        TODO("Not yet implemented")
    }

    actual suspend fun request(permission: AppPermission): PermissionStatus {
        TODO("Not yet implemented")
    }
}