@file:Suppress("unused")

package com.techullurgy.howzapp.base.network.di

import com.techullurgy.howzapp.base.network.http.KtorNetworkClient
import com.techullurgy.howzapp.core.network.http.SafeNetworkRequest
import com.techullurgy.howzapp.core.qualifiers.NonLocalNetwork
import com.techullurgy.howzapp.core.qualifiers.PublicNetwork
import io.ktor.client.HttpClient
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Singleton

@Module
internal class SafeNetworkRequestModule {

    @Singleton
    fun authEnabledSafeNetworkRequest(
        @Named(AuthEnabledHttpClient) client: HttpClient
    ): SafeNetworkRequest {
        return SafeNetworkRequest(KtorNetworkClient(client))
    }

    @Singleton
    @PublicNetwork
    fun publicSafeNetworkRequest(
        @Named(LocalHttpClient) client: HttpClient
    ): SafeNetworkRequest {
        return SafeNetworkRequest(KtorNetworkClient(client))
    }

    @Singleton
    @NonLocalNetwork
    fun nonLocalSafeNetworkRequest(
        @Named(NonLocalHttpClient) client: HttpClient
    ): SafeNetworkRequest {
        return SafeNetworkRequest(KtorNetworkClient(client))
    }
}