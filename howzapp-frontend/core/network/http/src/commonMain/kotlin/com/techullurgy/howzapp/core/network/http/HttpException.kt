package com.techullurgy.howzapp.core.network.http

class HttpException(
    val status: Int,
    val body: String? = null
) : Exception("HTTP $status: $body")