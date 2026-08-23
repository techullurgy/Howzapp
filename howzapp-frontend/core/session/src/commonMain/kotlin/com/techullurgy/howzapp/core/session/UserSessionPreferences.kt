package com.techullurgy.howzapp.core.session

import kotlinx.coroutines.flow.Flow

interface UserSessionPreferences {
    fun observeAuthInfo(): Flow<AuthInfo?>
    suspend fun setAuthInfo(authInfo: AuthInfo?)
}