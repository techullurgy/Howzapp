package com.techullurgy.howzapp.core.network.http

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen
import kotlinx.io.IOException
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class SafeNetworkRequest(
    @PublishedApi internal val client: NetworkClient
) {
    @PublishedApi
    internal inline fun <reified T: Any> internalSafeNetworkFlow(
        params: NetworkRequestParams,
        config: NetworkConfig = NetworkConfig()
    ): Flow<Result<T>> =
        flow {
            emit(client.execute<T>(params))
        }.retryWhen { cause, attempt ->
            if (
                cause.isRetryable() &&
                attempt < config.maxRetries
            ) {
                delay(
                    calculateRetryDelay(
                        attempt,
                        config
                    )
                )

                true
            } else {
                false
            }
        }.map<T, Result<T>> {
            Result.success(it)
        }.catch { cause ->
            if(cause is CancellationException) throw cause

            emit(Result.failure(cause))
        }

    @PublishedApi
    internal fun Throwable.isRetryable(): Boolean = when (this) {
        is IOException -> true

        is HttpException ->
            status == 429 || status in 500..599

        else -> false
    }

    @PublishedApi
    internal fun calculateRetryDelay(
        attempt: Long,
        config: NetworkConfig
    ): Duration {
        val exponent = attempt.coerceAtMost(30)

        return (config.retryDelay.inWholeMilliseconds * (1L shl exponent.toInt())).milliseconds
    }
}