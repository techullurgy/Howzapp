@file:Suppress("unused")

package com.techullurgy.howzapp.base.network.di

import com.techullurgy.howzapp.base.network.websockets.KtorWebSocketManager
import com.techullurgy.howzapp.common.json.HttpJson
import com.techullurgy.howzapp.common.websocket.ClientToServer
import com.techullurgy.howzapp.common.websocket.ServerToClient
import com.techullurgy.howzapp.common.json.WebSocketJson
import com.techullurgy.howzapp.core.network.http.AuthCacheClearer
import com.techullurgy.howzapp.core.network.websockets.WebSocketManager
import com.techullurgy.howzapp.core.qualifiers.HttpJson
import com.techullurgy.howzapp.core.qualifiers.WebSocketJson
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Singleton

@Module(includes = [HttpClientModule::class, SafeNetworkRequestModule::class])
@ComponentScan("com.techullurgy.howzapp.base.network")
class BaseNetworkModule {

    @Singleton
    fun websocketManager(
        client: HttpClient,
        @WebSocketJson json: Json
    ): WebSocketManager<ServerToClient, ClientToServer> {
        return KtorWebSocketManager(
            client = client,
            serialize = {
                val serializer = json.serializersModule.serializer<ServerToClient>()
                json.decodeFromString(serializer, it)
            },
            deserialize = {
                val serializer = json.serializersModule.serializer<ClientToServer>()
                json.encodeToString(serializer, it)
            }
        )
    }

    @Singleton
    @WebSocketJson
    fun webSocketJson(): Json = WebSocketJson

    @Singleton
    @HttpJson
    fun httpJson(): Json = HttpJson

    @Singleton
    fun authCacheClearer(
        @Named(AuthEnabledHttpClient) client: HttpClient
    ): AuthCacheClearer = AuthCacheClearer {
        client.authProvider<BearerAuthProvider>()?.clearToken()
    }
}
