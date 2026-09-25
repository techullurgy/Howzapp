package com.techullurgy.howzapp.core.network.http

import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.typeOf

data class TypeToken<T: Any>(val clazz: KClass<T>, val kType: KType? = null)

interface NetworkClient {
    suspend fun <T: Any> execute(
        params: NetworkRequestParams,
        typeToken: TypeToken<T>
    ): T
}

suspend inline fun <reified T: Any> NetworkClient.execute(
    params: NetworkRequestParams
) = execute(params, TypeToken(T::class, typeOfOrNull<T>()))

@PublishedApi
internal inline fun <reified T> typeOfOrNull(): KType? = try {
    // See Ktor TypeInfo implementation
    // R8 full mode strips type signature
    typeOf<T>()
} catch (_: Throwable) { null }