package com.techullurgy.howzapp.root.navigation.tests

import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.navigation3.ui.NavDisplay
import com.techullurgy.howzapp.core.navigation.AppNavKey
import com.techullurgy.howzapp.core.navigation.Navigator
import com.techullurgy.howzapp.root.navigation.core.AppNavigator
import com.techullurgy.howzapp.root.navigation.core.rememberNavigationState
import com.techullurgy.howzapp.root.navigation.core.toEntries
import org.junit.runner.RunWith
import org.koin.compose.koinInject
import org.koin.compose.module.rememberKoinModules
import org.koin.compose.navigation3.koinEntryProvider
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class, KoinExperimentalAPI::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class NavDisplayTest {

    @Test
    fun `koin Navigator Provider should work`() = runComposeUiTest {
        startKoin {  }

        setContent {
            val navigationState = rememberNavigationState(
                startRoute = RouteA,
                topLevelRoutes = listOf(RouteA, RouteB, RouteC, RouteD),
                savedStateConfiguration = savedStateConfiguration
            )

            key(navigationState) {
                rememberKoinModules(unloadModules = true) {
                    val navigationModule = module {
                        navigation<RouteA> {
                            val navigator = get<Navigator<AppNavKey>>()
                            Content("RouteA") { navigator.navigate(RouteB) }
                        }
                        navigation<RouteB> {
                            val navigator = get<Navigator<AppNavKey>>()
                            Content("RouteB") { navigator.navigate(RouteC) }
                        }
                        navigation<RouteC> {
                            val navigator = get<Navigator<AppNavKey>>()
                            Content("RouteC") { navigator.navigate(RouteD) }
                        }
                        navigation<RouteD> {
                            val navigator = get<Navigator<AppNavKey>>()
                            Content("RouteD") { navigator.navigate(RouteA) }
                        }
                    }

                    val navigatorModule = module {
                        single { AppNavigator(navigationState) }
                    }

                    listOf(navigationModule, navigatorModule)
                }
            }

            val navigator = koinInject<Navigator<AppNavKey>>()

            NavDisplay(
                entries = navigationState.toEntries(koinEntryProvider())
            ) {
                navigator.goBack()
            }
        }

        stopKoin()
    }
}

@Composable
private fun Content(
    text: String,
    onClick: () -> Unit = {}
) {
    BasicText(text, modifier = Modifier.clickable { onClick() })
}