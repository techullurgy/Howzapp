package com.techullurgy.howzapp.core.qualifiers

import org.koin.core.annotation.Named

@Named("PublicNetwork")
annotation class PublicNetwork

@Named("NonLocalNetwork")
annotation class NonLocalNetwork

@Named("WebSocketJson")
annotation class WebSocketJson

@Named("HttpJson")
annotation class HttpJson