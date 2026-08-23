package com.techullurgy.howzapp.core.domain

/**
 * Executes the given [action] if this is a [DomainResult.Success].
 * Returns the original result so you can chain these calls.
 */
inline fun <T> DomainResult<T>.onSuccess(action: (T) -> Unit): DomainResult<T> {
    if (this is DomainResult.Success) {
        action(data)
    }
    return this
}

/**
 * Executes the given [action] if this is a [DomainResult.Error].
 * Returns the original result so you can chain these calls.
 */
inline fun <T> DomainResult<T>.onError(action: (code: ErrorCode, cause: Throwable?) -> Unit): DomainResult<T> {
    if (this is DomainResult.Error) {
        action(code, cause)
    }
    return this
}

/**
 * Transforms a DomainResult into another type [R] by applying
 * [onSuccess] or [onError] depending on the state.
 */
inline fun <T, R> DomainResult<T>.fold(
    onSuccess: (T) -> R,
    onError: (code: ErrorCode, cause: Throwable?) -> R
): R {
    return when (this) {
        is DomainResult.Success -> onSuccess(data)
        is DomainResult.Error -> onError(code, cause)
    }
}