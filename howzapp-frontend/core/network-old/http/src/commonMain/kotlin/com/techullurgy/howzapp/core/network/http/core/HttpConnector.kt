package com.techullurgy.howzapp.core.network.http.core

import com.techullurgy.howzapp.core.domain.makeToken

interface HttpConnector<Request, Response> {
    suspend fun get(
        params: NetworkRequestParams.WithoutBody
    ): Response

    suspend fun post(
        params: NetworkRequestParams.WithBody<Request>
    ): Response

    suspend fun put(
        params: NetworkRequestParams.WithBody<Request>
    ): Response

    suspend fun delete(
        params: NetworkRequestParams.WithoutBody
    ): Response
}

@PublishedApi
context(client: NetworkClient)
internal inline fun <reified Request, reified Response> httpConnector(): HttpConnector<Request, Response> {
    return object : HttpConnector<Request, Response> {
        private val reqToken = makeToken<Request>()
        private val resToken = makeToken<Response>()

        override suspend fun get(
            params: NetworkRequestParams.WithoutBody
        ): Response = client.get(
            params = params,
            resToken = resToken
        )

        override suspend fun post(
            params: NetworkRequestParams.WithBody<Request>
        ): Response = client.post(
            params = params,
            reqToken = reqToken,
            resToken = resToken
        )

        override suspend fun put(
            params: NetworkRequestParams.WithBody<Request>
        ): Response = client.put(
            params = params,
            reqToken = reqToken,
            resToken = resToken
        )

        override suspend fun delete(
            params: NetworkRequestParams.WithoutBody
        ): Response = client.delete(
            params = params,
            resToken = resToken
        )
    }
}