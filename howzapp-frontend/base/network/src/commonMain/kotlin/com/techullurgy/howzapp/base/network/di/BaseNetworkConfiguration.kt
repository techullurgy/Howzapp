package com.techullurgy.howzapp.base.network.di

import com.techullurgy.howzapp.core.session.AuthInfo
import com.techullurgy.howzapp.core.session.UserSessionPreferences
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.json.Json
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Singleton
import org.koin.dsl.koinConfiguration
import org.koin.plugin.module.dsl.module

@Module
@ComponentScan("com.techullurgy.howzapp.base.network")
internal class BaseNetworkModule {
    @Singleton
    internal fun httpClient(
        engine: HttpClientEngine,
        @Provided sessionPreferences: UserSessionPreferences,
    ): HttpClient {
        return HttpClient(engine) {
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTP
                    host = "api.example.com"
                    path("/")
                }
            }

            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                    }
                )
            }

            install(HttpTimeout) {
                socketTimeoutMillis = 20_000
                requestTimeoutMillis = 20_000
                connectTimeoutMillis = 20_000
            }

            install(WebSockets) {
                pingIntervalMillis = 20_000
                contentConverter = KotlinxWebsocketSerializationConverter(Json)
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        sessionPreferences
                            .observeAuthInfo()
                            .firstOrNull()
                            ?.let {
                                BearerTokens(
                                    accessToken = it.accessToken,
                                    refreshToken = it.refreshToken
                                )
                            }
                    }

                    refreshTokens {
                        if(response.request.url.encodedPath.contains("auth/")) {
                            return@refreshTokens null
                        }

                        val authInfo = sessionPreferences.observeAuthInfo().firstOrNull()
                        if(authInfo?.refreshToken.isNullOrBlank()) {
                            sessionPreferences.setAuthInfo(null)
                            return@refreshTokens null
                        }

                        var bearerTokens: BearerTokens? = null
                        try {
                            val newAuthInfo = client.post(urlString = "") {
                                contentType(ContentType.Application.Json)
                                setBody(mapOf("refreshToken" to authInfo.refreshToken))
                                markAsRefreshTokenRequest()
                            }.body<AuthInfo>()

                            bearerTokens = BearerTokens(
                                accessToken = newAuthInfo.accessToken,
                                refreshToken = newAuthInfo.refreshToken
                            )
                        } catch (_: Exception) {
                            currentCoroutineContext().ensureActive()
                            sessionPreferences.setAuthInfo(null)
                        }

                        bearerTokens
                    }
                }
            }
        }
    }
}

val baseNetworkConfiguration = koinConfiguration {
    module<BaseNetworkModule>()
}