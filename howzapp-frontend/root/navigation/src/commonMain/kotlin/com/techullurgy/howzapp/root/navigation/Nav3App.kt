package com.techullurgy.howzapp.root.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.techullurgy.howzapp.core.navigation.AppNavKey
import com.techullurgy.howzapp.core.navigation.Navigator
import com.techullurgy.howzapp.root.navigation.core.AppNavigator
import com.techullurgy.howzapp.root.navigation.core.LocalNavigationState
import com.techullurgy.howzapp.root.navigation.core.LocalNavigator
import com.techullurgy.howzapp.root.navigation.core.PolymorphicModuleInstaller
import com.techullurgy.howzapp.root.navigation.core.rememberNavigationState
import com.techullurgy.howzapp.root.navigation.core.toEntries
import com.techullurgy.howzapp.root.navigation.scenedecorators.BottomNavigationSceneDecoratorStrategy
import com.techullurgy.howzapp.root.navigation.scenedecorators.DerivedKeySceneDecoratorStrategy
import com.techullurgy.howzapp.root.navigation.scenes.BottomTab
import com.techullurgy.howzapp.root.navigation.scenestrategies.HowzappHorizontalPagerSceneStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.serializer
import org.koin.compose.koinInject
import org.koin.compose.module.rememberKoinModules
import org.koin.compose.navigation3.koinEntryProvider
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module

@OptIn(KoinExperimentalAPI::class, ExperimentalSerializationApi::class)
@Composable
fun Nav3App(
    startRoute: AppNavKey = Home,
    bottomTabs: List<BottomTab> = BottomTabs,
    installer: PolymorphicModuleInstaller = providePolymorphicModuleBuilder()
) {
    val navigationState = rememberNavigationState(
        startRoute = startRoute,
        topLevelRoutes = bottomTabs.map { it.route },
        savedStateConfiguration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(AppNavKey::class) {
                    installer()
                }
            }
        }
    )

    key(navigationState) {
        val navigationModule = LocalKoinNavigationModule.current

        rememberKoinModules(
            unloadOnForgotten = true,
            unloadOnAbandoned = true,
            unloadModules = true
        ) {
            val navigatorModule = module {
                single<Navigator<AppNavKey>> { AppNavigator(navigationState) }
            }

            listOf(
                navigationModule,
                navigatorModule
            )
        }
    }


    CompositionLocalProvider(
        LocalNavigationState provides navigationState,
        LocalNavigator provides koinInject()
    ) {
        val navigator = LocalNavigator.current

        val howzappHorizontalPagerSceneStrategy = remember {
            HowzappHorizontalPagerSceneStrategy(bottomTabs.map { it.route })
        }

        val bottomNavigationSceneDecoratorStrategy = remember {
            BottomNavigationSceneDecoratorStrategy<AppNavKey>(bottomTabs)
        }

        val derivedKeySceneDecoratorStrategy = remember { DerivedKeySceneDecoratorStrategy<AppNavKey>() }

        NavDisplay(
            entries = navigationState.toEntries(koinEntryProvider()),
            sceneStrategies = listOf(howzappHorizontalPagerSceneStrategy),
            sceneDecoratorStrategies = listOf(
                derivedKeySceneDecoratorStrategy,
                bottomNavigationSceneDecoratorStrategy
            ),
            onBack = navigator::goBack,
            modifier = Modifier.fillMaxSize()
        )
    }
}

private val rootNavigationModule = module {
    // includes() // - Every Feature's NavigationModule
}

val LocalKoinNavigationModule = staticCompositionLocalOf { rootNavigationModule }

@OptIn(ExperimentalSerializationApi::class)
private fun providePolymorphicModuleBuilder(): PolymorphicModuleInstaller = {
    subclassesOfSealed(serializer<RootNavKey>())
}

@Serializable
private sealed interface RootNavKey: AppNavKey

@Serializable data object Home: RootNavKey
@Serializable data object Chats: RootNavKey
@Serializable data object Status: RootNavKey
@Serializable data object Calls: RootNavKey

private val BottomTabs = listOf(
    BottomTab(
        label = "Home",
        route = Home,
    ),
    BottomTab(
        label = "Chats",
        route = Chats,
    ),
    BottomTab(
        label = "Status",
        route = Status,
    ),
    BottomTab(
        label = "Calls",
        route = Calls,
    )
)

val j = ImageVector.Builder(
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    path(fill = SolidColor(Color.Yellow)) {
        
    }
}