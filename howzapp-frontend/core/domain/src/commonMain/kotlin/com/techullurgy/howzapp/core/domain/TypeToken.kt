package com.techullurgy.howzapp.core.domain

import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.typeOf

data class TypeToken(
    val kClass: KClass<*>,
    val kType: KType
)

// Helper to capture the types at compile time
inline fun <reified T> makeToken(): TypeToken = TypeToken(T::class, typeOf<T>())
