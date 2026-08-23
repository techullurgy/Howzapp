package com.techullurgy.howzapp.core.network.http.core

sealed interface NetworkRequestParams {
    val url: String
    val headers: Map<String, String>?
    val queryParams: Map<String, String>?

    data class WithBody<T>(
        override val url: String,
        override val headers: Map<String, String>? = null,
        override val queryParams: Map<String, String>? = null,
        val body: T?
    ): NetworkRequestParams

    data class WithoutBody(
        override val url: String,
        override val headers: Map<String, String>? = null,
        override val queryParams: Map<String, String>? = null,
    ): NetworkRequestParams
}