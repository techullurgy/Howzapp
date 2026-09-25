@file:Suppress("unused")

package com.techullurgy.howzapp.base.network.di

import com.techullurgy.howzapp.base.network.http.AuthConfigurer
import com.techullurgy.howzapp.core.qualifiers.HttpJson
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Singleton

@Module
internal class HttpClientModule {

    @Named(AuthEnabledHttpClient)
    @Singleton
    fun authEnabledHttpClient(
        @Named(LocalHttpClient) client: HttpClient,
        authConfigurer: AuthConfigurer
    ): HttpClient = client.config {
        install(Auth) { authConfigurer.configure(this) }
    }

    @Named(LocalHttpClient)
    @Singleton
    fun localHttpClient(
        engine: HttpClientEngine,
        @HttpJson json: Json
    ) = baseHttpClient(engine).config {
        install(ContentNegotiation) { json(json) }

        install(WebSockets) {
            pingIntervalMillis = 10_000
        }
    }

    @Named(NonLocalHttpClient)
    @Singleton
    fun nonLocalHttpClient(
        engine: HttpClientEngine
    ) = baseHttpClient(engine).config {
        install(HttpTimeout) {
            // Both are Useful for S3 Uploads
            requestTimeoutMillis = 0
            socketTimeoutMillis = 30_000
        }
    }

    private fun baseHttpClient(
        engine: HttpClientEngine
    ): HttpClient = HttpClient(engine)
}

internal const val AuthEnabledHttpClient = "AuthEnabledHttpClient"
internal const val LocalHttpClient = "LocalHttpClient"
internal const val NonLocalHttpClient = "NonLocalHttpClient"