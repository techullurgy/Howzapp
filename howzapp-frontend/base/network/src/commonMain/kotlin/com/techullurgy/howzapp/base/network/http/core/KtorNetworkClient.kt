package com.techullurgy.howzapp.base.network.http.core

import com.techullurgy.howzapp.core.domain.TypeToken
import com.techullurgy.howzapp.core.network.http.core.NetworkClient
import com.techullurgy.howzapp.core.network.http.core.NetworkRequestParams
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.util.reflect.TypeInfo
import org.koin.core.annotation.Singleton

@Singleton(binds = [NetworkClient::class])
internal class KtorNetworkClient(
    private val http: HttpClient
): NetworkClient {
    private fun TypeToken.toKtorType(): TypeInfo = TypeInfo(this.kClass, this.kType)

    override suspend fun <Res> get(
        params: NetworkRequestParams.WithoutBody,
        resToken: TypeToken
    ): Res {
        return http.get(params.url) {
            applyParamsAndHeaders(params.queryParams, params.headers)
        }.body(resToken.toKtorType())
    }

    override suspend fun <Req, Res> post(
        params: NetworkRequestParams.WithBody<Req>,
        reqToken: TypeToken,
        resToken: TypeToken
    ): Res {
        return http.post(params.url) {
            applyParamsAndHeaders(params.queryParams, params.headers)
            contentType(ContentType.Application.Json)
            setBody(params.body, reqToken.toKtorType())
        }.body(resToken.toKtorType()) as Res
    }

    override suspend fun <Req, Res> put(
        params: NetworkRequestParams.WithBody<Req>,
        reqToken: TypeToken,
        resToken: TypeToken
    ): Res {
        return http.put(params.url) {
            applyParamsAndHeaders(params.queryParams, params.headers)
            contentType(ContentType.Application.Json)
            setBody(params.body, reqToken.toKtorType())
        }.body(resToken.toKtorType()) as Res
    }

    override suspend fun <Res> delete(
        params: NetworkRequestParams.WithoutBody,
        resToken: TypeToken
    ): Res {
        return http.delete(params.url) {
            applyParamsAndHeaders(params.queryParams, params.headers)
        }.body(resToken.toKtorType()) as Res
    }

    // Helper to apply maps to Ktor's builder
    private fun HttpRequestBuilder.applyParamsAndHeaders(
        queryParams: Map<String, String>?,
        headers: Map<String, String>?
    ) {
        queryParams?.forEach { (key, value) -> parameter(key, value) }
        headers?.forEach { (key, value) -> header(key, value) }
    }
}