package com.techullurgy.howzapp.core.domain

sealed interface DomainResult<out T> {
    data class Success<out T>(val data: T): DomainResult<T>

    data class Error(val code: ErrorCode, val cause: Throwable? = null): DomainResult<Nothing>
}