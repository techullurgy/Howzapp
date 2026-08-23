package com.techullurgy.howzapp.feature.chats.di

import com.techullurgy.howzapp.feature.chats.data.di.ChatDataModule
import com.techullurgy.howzapp.feature.chats.presentation.impl.di.ChatPresentationModule
import com.techullurgy.howzapp.feature.chats.presentation.impl.di.chatNavigationModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton
import org.koin.dsl.koinConfiguration
import org.koin.plugin.module.dsl.module

@Module(includes = [ChatDataModule::class, ChatPresentationModule::class])
internal class ChatFeatureModule {
    @Singleton
    fun provideCoroutineScope(): CoroutineScope = CoroutineScope(SupervisorJob())
}

val chatFeatureConfiguration = koinConfiguration {
    module<ChatFeatureModule>()
    modules(chatNavigationModule)
}