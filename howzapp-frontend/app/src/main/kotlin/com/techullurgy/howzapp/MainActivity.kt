package com.techullurgy.howzapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.ui.NavDisplay
import org.koin.compose.navigation3.koinEntryProvider
import org.koin.core.annotation.KoinExperimentalAPI
import com.techullurgy.howzapp.feature.chats.presentation.api.ChatRoute

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }
}

@OptIn(KoinExperimentalAPI::class)
@Composable
fun App() {
    val entryProvider = koinEntryProvider<Any>()

    val backStack = remember { mutableStateListOf(ChatRoute.ConversationRoute("11")) }

    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider
    )
}