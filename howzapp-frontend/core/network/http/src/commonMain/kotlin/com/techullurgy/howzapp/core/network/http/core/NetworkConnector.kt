package com.techullurgy.howzapp.core.network.http.core

import com.techullurgy.howzapp.core.domain.DomainResult
import kotlinx.coroutines.flow.Flow

interface NetworkConnector {
    fun <Remote, Domain> safeNetworkApiFlow(
        maxRetries: Long = 3,
        initialDelayMillis: Long = 500L,
        maxDelayMillis: Long = 3000L,
        delayFactor: Double = 2.0,
        errorToDomain: ((Throwable) -> DomainResult<Nothing>)? = null,
        mapToDomain: (Remote) -> Domain,
        apiCall: suspend context(NetworkClient) () -> Remote
    ): Flow<DomainResult<Domain>>
}