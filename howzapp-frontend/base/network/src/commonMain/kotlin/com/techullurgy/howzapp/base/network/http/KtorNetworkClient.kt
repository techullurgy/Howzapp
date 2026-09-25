package com.techullurgy.howzapp.base.network.http

import com.techullurgy.howzapp.core.network.http.HttpException
import com.techullurgy.howzapp.core.network.http.NetworkClient
import com.techullurgy.howzapp.core.network.http.NetworkRequestMethod
import com.techullurgy.howzapp.core.network.http.NetworkRequestParams
import com.techullurgy.howzapp.core.network.http.TypeToken
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.util.reflect.TypeInfo

class KtorNetworkClient(
    private val client: HttpClient
): NetworkClient {
    override suspend fun <T : Any> execute(
        params: NetworkRequestParams,
        typeToken: TypeToken<T>
    ): T {
        val response = client.request {
            params.applyTo(this)
        }

        if (!response.status.isSuccess()) {
            throw HttpException(
                status = response.status.value,
                body = response.bodyAsText()
            )
        }

        // val type = typeInfo<T>()
        val typeInfo = TypeInfo(
            type = typeToken.clazz,
            kotlinType = typeToken.kType
        )
        return response.body(typeInfo) as T
    }

    @PublishedApi
    internal fun NetworkRequestParams.applyTo(builder: HttpRequestBuilder) {
        with(builder) {
            url(this@applyTo.url)

            method = when(this@applyTo.method) {
                NetworkRequestMethod.GET -> HttpMethod.Get
                NetworkRequestMethod.POST -> HttpMethod.Post
                NetworkRequestMethod.PUT -> HttpMethod.Put
                NetworkRequestMethod.DELETE -> HttpMethod.Delete
            }

            this@applyTo.headers.forEach { (key, value) ->
                header(key, value)
            }

            this@applyTo.queryParams.forEach { (key, value) ->
                parameter(key, value)
            }

            if(this@applyTo is NetworkRequestParams.WithBody<*>) {
                if(!this@applyTo.headers.containsKey(HttpHeaders.ContentType)) {
                    contentType(ContentType.Application.Json)
                }
                setBody(this@applyTo.body)
            }
        }
    }
}