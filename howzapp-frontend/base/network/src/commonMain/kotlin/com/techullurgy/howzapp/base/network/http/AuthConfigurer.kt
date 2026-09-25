@file:Suppress("unused")

package com.techullurgy.howzapp.base.network.http

import io.ktor.client.plugins.auth.AuthConfig
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.request.post
import org.koin.core.annotation.Singleton

interface AuthConfigurer {
    fun configure(config: AuthConfig)
}

@Singleton
internal class KtorAuthConfigurer: AuthConfigurer {
    override fun configure(
        config: AuthConfig
    ) {
        with(config) {
            bearer {
                loadTokens { TODO() }
                refreshTokens {
                    client.post {
                        markAsRefreshTokenRequest()
                    }
                    TODO()
                }
            }
        }
    }
}