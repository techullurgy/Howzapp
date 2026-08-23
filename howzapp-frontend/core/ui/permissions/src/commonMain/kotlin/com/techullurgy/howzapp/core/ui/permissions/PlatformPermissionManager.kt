package com.techullurgy.howzapp.core.ui.permissions

expect class PlatformPermissionManager {
    suspend fun check(
        permission: AppPermission
    ): PermissionStatus

    suspend fun request(
        permission: AppPermission
    ): PermissionStatus
}