package com.techullurgy.howzapp

import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.annotation.Single

@Single
class DownloadRepository {
    val flow = MutableStateFlow(0)
}