package com.techullurgy.howzapp.core.network.http.core

import com.techullurgy.howzapp.core.domain.TypeToken

interface NetworkClient {
    suspend fun <Res> get(
        params: NetworkRequestParams.WithoutBody,
        resToken: TypeToken,
    ): Res

    suspend fun <Req, Res> post(
        params: NetworkRequestParams.WithBody<Req>,
        reqToken: TypeToken,
        resToken: TypeToken
    ): Res

    suspend fun <Req, Res> put(
        params: NetworkRequestParams.WithBody<Req>,
        reqToken: TypeToken,
        resToken: TypeToken
    ): Res

    suspend fun <Res> delete(
        params: NetworkRequestParams.WithoutBody,
        resToken: TypeToken
    ): Res
}