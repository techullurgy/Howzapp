package com.techullurgy.howzapp.base.network.http.core

import com.techullurgy.howzapp.core.domain.DomainResult
import com.techullurgy.howzapp.core.network.http.core.NetworkClient
import com.techullurgy.howzapp.core.network.http.core.NetworkConnector
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Singleton

@Singleton(binds = [NetworkConnector::class])
internal class KtorNetworkConnector(
    private val networkClient: NetworkClient
): KtorHttpApiSource(), NetworkConnector {
    override fun <Remote, Domain> safeNetworkApiFlow(
        maxRetries: Long,
        initialDelayMillis: Long,
        maxDelayMillis: Long,
        delayFactor: Double,
        errorToDomain: ((Throwable) -> DomainResult<Nothing>)?,
        mapToDomain: (Remote) -> Domain,
        apiCall: suspend context(NetworkClient) () -> Remote
    ): Flow<DomainResult<Domain>> {
        return safeApiFlow(
            maxRetries = maxRetries,
            initialDelayMillis = initialDelayMillis,
            maxDelayMillis = maxDelayMillis,
            delayFactor = delayFactor,
            errorToDomain = errorToDomain ?: { defaultHandleError(it) },
            mapToDomain = mapToDomain,
            apiCall = {
                context(networkClient) {
                    apiCall()
                }
            }
        )
    }
}

