package com.techullurgy.howzapp.core.files

expect object PlatformFileFactory {
    fun create(identifier: String): PlatformFile
}