package com.techullurgy.howzapp.core.files

internal actual fun js.buffer.DataView<js.buffer.ArrayBuffer>.toByteArray(): ByteArray {
    val length = this.byteLength
    return ByteArray(length) { i ->
        this.getInt8(i)
    }
}