package com.techullurgy.howzapp.root.navigation.tests

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.v2.runComposeUiTest
import com.techullurgy.howzapp.core.navigation.AppNavKey
import com.techullurgy.howzapp.core.navigation.Navigator
import com.techullurgy.howzapp.core.navigation.TopLevelDestination
import com.techullurgy.howzapp.root.navigation.LocalKoinNavigationModule
import com.techullurgy.howzapp.root.navigation.Nav3App
import com.techullurgy.howzapp.root.navigation.scenes.BottomTab
import kotlinx.serialization.ExperimentalSerializationApi
import org.junit.runner.RunWith
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class, ExperimentalSerializationApi::class, KoinExperimentalAPI::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class Nav3AppTest {

    @Test
    fun `Nav3App should work as expected`() = runComposeUiTest {
        startKoin {  }

        val koinNavigationModule = module {
            navigation<RouteA>(
                metadata = TopLevelDestination.applyAsDestination()
            ) {
                val navigator = get<Navigator<AppNavKey>>()
                ColorContent(Color.Green, "RouteA Content") {
                    navigator.navigate(RouteB)
                }
            }

            navigation<RouteB>(
                metadata = TopLevelDestination.applyAsDestination()
            ) {
                val navigator = get<Navigator<AppNavKey>>()
                ColorContent(Color.Cyan, "RouteB Content") {
                    navigator.navigate(RouteC)
                }
            }

            navigation<RouteC>(
                metadata = TopLevelDestination.applyAsDestination()
            ) {
                val navigator = get<Navigator<AppNavKey>>()
                ColorContent(Color.Magenta, "RouteC Content") {
                    navigator.navigate(RouteD)
                }
            }

            navigation<RouteD>(
                metadata = TopLevelDestination.applyAsDestination()
            ) {
                val navigator = get<Navigator<AppNavKey>>()
                ColorContent(Color.Blue, "RouteD Content") {
                    navigator.navigate(RouteA)
                }
            }
        }

        setContent {
            CompositionLocalProvider(
                LocalKoinNavigationModule provides koinNavigationModule
            ) {
                Nav3App(
                    startRoute = RouteA,
                    bottomTabs = listOf(
                        BottomTab("RouteA", RouteA),
                        BottomTab("RouteB", RouteB),
                        BottomTab("RouteC", RouteC),
                        BottomTab("RouteD", RouteD)
                    ),
                    installer = { subclassesOfSealed<AppTestKey>() }
                )
            }
        }

        stopKoin()
    }
}

@Composable
private fun ColorContent(
    color: Color,
    text: String,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize().background(color),
        contentAlignment = Alignment.Center
    ) {
        BasicText(text, modifier = Modifier.clickable { onClick() })
    }
}