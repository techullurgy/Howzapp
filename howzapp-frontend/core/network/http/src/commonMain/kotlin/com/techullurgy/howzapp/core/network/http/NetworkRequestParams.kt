package com.techullurgy.howzapp.core.network.http

sealed interface NetworkRequestParams {
    val url: String
    val method: NetworkRequestMethod
    val queryParams: Map<String, Any>
    val headers: Map<String, Any>

    data class WithBody<T>(
        override val url: String,
        override val method: NetworkRequestMethod,
        override val queryParams: Map<String, Any> = emptyMap(),
        override val headers: Map<String, Any> = emptyMap(),
        val body: T?
    ): NetworkRequestParams

    data class WithoutBody(
        override val url: String,
        override val method: NetworkRequestMethod,
        override val queryParams: Map<String, Any> = emptyMap(),
        override val headers: Map<String, Any> = emptyMap(),
    ): NetworkRequestParams
}