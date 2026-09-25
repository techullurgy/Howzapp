package com.techullurgy.howzapp.core.network.http.core

import com.techullurgy.howzapp.core.domain.DomainResult
import com.techullurgy.howzapp.core.domain.ErrorCode
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen
import kotlinx.io.IOException
import kotlin.math.pow
import kotlin.time.Duration.Companion.milliseconds

abstract class BaseHttpApiSource {
    protected fun <Remote, Domain> safeApiFlow(
        maxRetries: Long = 3,
        initialDelayMillis: Long = 500L,
        maxDelayMillis: Long = 3000L,
        delayFactor: Double = 2.0,
        // Allow callers to pass a custom error handler, falling back to the default
        errorToDomain: (Throwable) -> DomainResult<Nothing> = { defaultHandleError(it) },
        mapToDomain: (Remote) -> Domain,
        apiCall: suspend () -> Remote
    ): Flow<DomainResult<Domain>> = flow<DomainResult<Domain>> {
        // 1. Execute the call and emit Success if it works
        val response = apiCall()
        emit(DomainResult.Success(mapToDomain(response)))
    }.retryWhen { cause, attempt ->
        // 2. Intercept exceptions. Return 'true' to retry, 'false' to fail.
        if (attempt < maxRetries && shouldRetry(cause)) {
            val delayTime = (initialDelayMillis * delayFactor.pow(attempt.toDouble())).toLong()
                .coerceAtMost(maxDelayMillis)
            delay(delayTime.milliseconds)
            true
        } else {
            false
        }
    }.catch { e ->
        // 3. Catch the final failure and use the injected errorMapper
        emit(errorToDomain(e))
    }

    protected open fun shouldRetry(e: Throwable): Boolean {
        return e is IOException
    }

    protected open fun defaultHandleError(e: Throwable): DomainResult<Nothing> {
        return when (e) {
            is IOException -> DomainResult.Error(ErrorCode.NoNetworkConnection, e)
            else -> DomainResult.Error(ErrorCode.Unknown(e.message ?: "Unknown error"), e)
        }
    }
}