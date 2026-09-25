package com.techullurgy.howzapp.common.json

import kotlinx.serialization.json.Json

val HttpJson = Json {
    ignoreUnknownKeys = true
}