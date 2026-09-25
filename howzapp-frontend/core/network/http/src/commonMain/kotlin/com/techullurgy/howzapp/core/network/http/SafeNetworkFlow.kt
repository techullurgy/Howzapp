package com.techullurgy.howzapp.core.network.http

import kotlinx.coroutines.flow.Flow

context(request: SafeNetworkRequest)
inline fun <reified T: Any> safeNetworkFlow(
    params: NetworkRequestParams,
    config: NetworkConfig = NetworkConfig()
): Flow<Result<T>> {
    return request.internalSafeNetworkFlow<T>(params, config)
}
