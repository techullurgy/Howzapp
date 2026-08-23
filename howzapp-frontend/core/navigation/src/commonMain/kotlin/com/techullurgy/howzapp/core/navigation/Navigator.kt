package com.techullurgy.howzapp.core.navigation

interface Navigator<T: AppNavKey> {
    fun goBack()
    fun navigate(destination: T)
}