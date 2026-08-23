package com.techullurgy.howzapp.core.domain

sealed interface AppWebsocketEvent<out T> {
    data class Data<T>(val data: T): AppWebsocketEvent<T>
    data object Failed: AppWebsocketEvent<Nothing>
}