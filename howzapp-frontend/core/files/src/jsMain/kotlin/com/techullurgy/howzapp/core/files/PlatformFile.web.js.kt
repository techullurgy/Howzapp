package com.techullurgy.howzapp.core.files

import js.buffer.ArrayBuffer
import js.buffer.DataView

internal actual fun DataView<ArrayBuffer>.toByteArray(): ByteArray {
    val length = this.byteLength
    return ByteArray(length) { i ->
        this.getInt8(i)
    }
}