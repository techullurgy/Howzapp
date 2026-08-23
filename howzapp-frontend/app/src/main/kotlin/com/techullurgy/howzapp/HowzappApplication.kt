package com.techullurgy.howzapp

import android.app.Application
import com.techullurgy.howzapp.database.core.HowzappDatabase
import com.techullurgy.howzapp.database.core.di.MainDatabaseModule
import com.techullurgy.howzapp.feature.chats.di.chatFeatureConfiguration
import org.koin.android.ext.koin.androidContext
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module
import org.koin.dsl.includes
import org.koin.dsl.koinConfiguration
import org.koin.plugin.module.dsl.module
import org.koin.plugin.module.dsl.startKoin

@Module(includes = [MainDatabaseModule::class])
@Configuration
@ComponentScan
class AppModule

@KoinApplication
class KoinApp

class HowzappApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin<KoinApp> {
            androidContext(this@HowzappApplication)

            includes(chatFeatureConfiguration)
        }
    }
}