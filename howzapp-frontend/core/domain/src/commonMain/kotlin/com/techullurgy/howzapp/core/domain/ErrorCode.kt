package com.techullurgy.howzapp.core.domain

sealed interface ErrorCode {
    data object NoNetworkConnection: ErrorCode
    data class Unknown(val message: String): ErrorCode
}