package com.techullurgy.howzapp.base.network.http.core

import com.techullurgy.howzapp.core.domain.DomainResult
import com.techullurgy.howzapp.core.domain.ErrorCode
import com.techullurgy.howzapp.core.network.http.core.BaseHttpApiSource
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException

internal abstract class KtorHttpApiSource(): BaseHttpApiSource() {
    override fun shouldRetry(e: Throwable): Boolean {
        // Retry for Ktor specific timeouts and 5xx server errors,
        // or fallback to the base class rules (IOException)
        return e is HttpRequestTimeoutException ||
                (e is ResponseException && e.response.status.value in 500..599) ||
                super.shouldRetry(e)
    }

    override fun defaultHandleError(e: Throwable): DomainResult<Nothing> {
        return when (e) {
            is ResponseException -> {
                // Map Ktor HTTP errors
                DomainResult.Error(
                    code = ErrorCode.Unknown(e.response.status.description),
                    cause = e
                )
            }
            // Delegate IOExceptions and everything else to the base class
            else -> super.defaultHandleError(e)
        }
    }
}