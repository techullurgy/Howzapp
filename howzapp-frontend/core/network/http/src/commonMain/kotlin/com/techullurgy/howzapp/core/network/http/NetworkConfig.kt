package com.techullurgy.howzapp.core.network.http

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

data class NetworkConfig(
    val maxRetries: Int = 0,
    val retryDelay: Duration = 500.milliseconds
)