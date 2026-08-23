package com.techullurgy.howzapp.feature.chats.presentation.impl.di

import com.techullurgy.howzapp.feature.chats.presentation.api.ChatRoute
import com.techullurgy.howzapp.feature.chats.presentation.api.screens.IConversationScreen
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

@OptIn(KoinExperimentalAPI::class)
val chatNavigationModule = module {
    navigation<ChatRoute.ConversationRoute> {
        get<IConversationScreen>().invoke(it.conversationId)
    }
}