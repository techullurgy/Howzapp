package com.techullurgy.howzapp.root.navigation.tests

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.navigation3.runtime.NavBackStack
import androidx.savedstate.serialization.SavedStateConfiguration
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.techullurgy.howzapp.core.navigation.AppNavKey
import com.techullurgy.howzapp.core.navigation.Navigator
import com.techullurgy.howzapp.root.navigation.core.AppNavigator
import com.techullurgy.howzapp.root.navigation.core.NavigationState
import com.techullurgy.howzapp.root.navigation.core.rememberNavigationState
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class NavigationStateTest {

    @Test
    fun `initial navigation backstack and topLevelRoute should be correct`() = runComposeUiTest {
        lateinit var navigationState: NavigationState

        setContent {
            navigationState = rememberNavigationState(
                RouteA,
                listOf(RouteA, RouteB, RouteC, RouteD),
                savedStateConfiguration = savedStateConfiguration
            )
        }

        runOnIdle {
            assertThat(navigationState.currentBackStack.toList())
                .isEqualTo(NavBackStack(RouteA, RouteB, RouteC, RouteD).toList())

            assertThat(navigationState.topLevelRoute)
                .isEqualTo(RouteA)
        }
    }

    @Test
    fun `navigator navigation-1 should be correct`() = runComposeUiTest {
        lateinit var navigationState: NavigationState
        lateinit var navigator: Navigator<AppNavKey>

        setContent {
            navigationState = rememberNavigationState(
                RouteA,
                listOf(RouteA, RouteB, RouteC, RouteD),
                savedStateConfiguration = savedStateConfiguration
            )

            navigator = AppNavigator(navigationState)
        }

        runOnIdle {
            navigator.navigate(RouteA1)
            assertThat(navigationState.currentBackStack.toList())
                .isEqualTo(NavBackStack(RouteA, RouteA1).toList())

            navigator.navigate(RouteA2)
            assertThat(navigationState.currentBackStack.toList())
                .isEqualTo(NavBackStack(RouteA, RouteA1, RouteA2).toList())

            navigator.navigate(RouteA3)
            assertThat(navigationState.currentBackStack.toList())
                .isEqualTo(NavBackStack(RouteA, RouteA1, RouteA2, RouteA3).toList())

            navigator.navigate(RouteB2)
            assertThat(navigationState.currentBackStack.toList())
                .isEqualTo(NavBackStack(RouteA, RouteA1, RouteA2, RouteA3, RouteB2).toList())

            navigator.navigate(RouteB3)
            assertThat(navigationState.currentBackStack.toList())
                .isEqualTo(NavBackStack(RouteA, RouteA1, RouteA2, RouteA3, RouteB2, RouteB3).toList())

            navigator.goBack()
            assertThat(navigationState.currentBackStack.toList())
                .isEqualTo(NavBackStack(RouteA, RouteA1, RouteA2, RouteA3, RouteB2).toList())

            navigator.goBack()
            assertThat(navigationState.currentBackStack.toList())
                .isEqualTo(NavBackStack(RouteA, RouteA1, RouteA2, RouteA3).toList())

            navigator.goBack()
            assertThat(navigationState.currentBackStack.toList())
                .isEqualTo(NavBackStack(RouteA, RouteA1, RouteA2).toList())

            navigator.goBack()
            assertThat(navigationState.currentBackStack.toList())
                .isEqualTo(NavBackStack(RouteA, RouteA1).toList())

            navigator.goBack()
            assertThat(navigationState.currentBackStack.toList())
                .isEqualTo(NavBackStack(RouteA, RouteB, RouteC, RouteD).toList())

            assertThat(navigationState.topLevelRoute)
                .isEqualTo(RouteA)

            navigator.goBack()
            assertThat(navigationState.currentBackStack.toList())
                .isEqualTo(emptyList())
        }
    }

    @Test
    fun `navigator navigation-2 should be correct`() = runComposeUiTest {
        lateinit var navigationState: NavigationState
        lateinit var navigator: Navigator<AppNavKey>

        setContent {
            navigationState = rememberNavigationState(
                RouteA,
                listOf(RouteA, RouteB, RouteC, RouteD),
                savedStateConfiguration = savedStateConfiguration
            )

            navigator = AppNavigator(navigationState)
        }

        runOnIdle {
            navigator.navigate(RouteC)
            assertThat(navigationState.topLevelRoute)
                .isEqualTo(RouteC)

            navigator.navigate(RouteC1)
            assertThat(navigationState.currentBackStack.toList())
                .isEqualTo(NavBackStack(RouteC, RouteC1).toList())

            navigator.navigate(RouteC2)
            assertThat(navigationState.currentBackStack.toList())
                .isEqualTo(NavBackStack(RouteC, RouteC1, RouteC2).toList())

            navigator.navigate(RouteC3)
            assertThat(navigationState.currentBackStack.toList())
                .isEqualTo(NavBackStack(RouteC, RouteC1, RouteC2, RouteC3).toList())

            navigator.navigate(RouteB2)
            assertThat(navigationState.currentBackStack.toList())
                .isEqualTo(NavBackStack(RouteC, RouteC1, RouteC2, RouteC3, RouteB2).toList())

            navigator.navigate(RouteB3)
            assertThat(navigationState.currentBackStack.toList())
                .isEqualTo(NavBackStack(RouteC, RouteC1, RouteC2, RouteC3, RouteB2, RouteB3).toList())

            navigator.goBack()
            assertThat(navigationState.currentBackStack.toList())
                .isEqualTo(NavBackStack(RouteC, RouteC1, RouteC2, RouteC3, RouteB2).toList())

            navigator.goBack()
            assertThat(navigationState.currentBackStack.toList())
                .isEqualTo(NavBackStack(RouteC, RouteC1, RouteC2, RouteC3).toList())

            navigator.goBack()
            assertThat(navigationState.currentBackStack.toList())
                .isEqualTo(NavBackStack(RouteC, RouteC1, RouteC2).toList())

            navigator.goBack()
            assertThat(navigationState.currentBackStack.toList())
                .isEqualTo(NavBackStack(RouteC, RouteC1).toList())

            navigator.goBack()
            assertThat(navigationState.currentBackStack.toList())
                .isEqualTo(NavBackStack(RouteA, RouteB, RouteC, RouteD).toList())

            assertThat(navigationState.topLevelRoute)
                .isEqualTo(RouteC)

            navigator.goBack()
            assertThat(navigationState.currentBackStack.toList())
                .isEqualTo(NavBackStack(RouteA, RouteB, RouteC, RouteD).toList())

            assertThat(navigationState.topLevelRoute)
                .isEqualTo(RouteA)

            navigator.goBack()
            assertThat(navigationState.currentBackStack.toList())
                .isEqualTo(emptyList())
        }
    }

    @Test
    fun `navigator navigation-3 should be correct`() = runComposeUiTest {
        lateinit var navigationState: NavigationState
        lateinit var navigator: Navigator<AppNavKey>

        setContent {
            navigationState = rememberNavigationState(
                RouteA,
                listOf(RouteA, RouteB, RouteC, RouteD),
                savedStateConfiguration = savedStateConfiguration
            )

            navigator = AppNavigator(navigationState)
        }

        runOnIdle {
            assertThat(navigationState.currentBackStack.toList())
                .isEqualTo(NavBackStack(RouteA, RouteB, RouteC, RouteD).toList())

            assertThat(navigationState.topLevelRoute)
                .isEqualTo(RouteA)

            navigator.navigate(RouteD)
            navigator.navigate(RouteC)
            navigator.navigate(RouteD)
            navigator.navigate(RouteC)
            navigator.navigate(RouteD)

            assertThat(navigationState.currentBackStack.toList())
                .isEqualTo(NavBackStack(RouteA, RouteB, RouteC, RouteD).toList())

            assertThat(navigationState.topLevelRoute)
                .isEqualTo(RouteD)

            navigator.goBack()
            assertThat(navigationState.currentBackStack.toList())
                .isEqualTo(NavBackStack(RouteA, RouteB, RouteC, RouteD).toList())

            assertThat(navigationState.topLevelRoute)
                .isEqualTo(RouteA)

            navigator.goBack()
            assertThat(navigationState.currentBackStack.toList())
                .isEqualTo(emptyList())
        }
    }
}

@Serializable sealed interface AppTestKey: AppNavKey

@Serializable data object RouteA: AppTestKey
@Serializable data object RouteB: AppTestKey
@Serializable data object RouteC: AppTestKey
@Serializable data object RouteD: AppTestKey

@Serializable data object RouteA1: AppTestKey
@Serializable data object RouteA2: AppTestKey
@Serializable data object RouteA3: AppTestKey

@Serializable data object RouteB1: AppTestKey
@Serializable data object RouteB2: AppTestKey
@Serializable data object RouteB3: AppTestKey

@Serializable data object RouteC1: AppTestKey
@Serializable data object RouteC2: AppTestKey
@Serializable data object RouteC3: AppTestKey

@Serializable data object RouteD1: AppTestKey
@Serializable data object RouteD2: AppTestKey
@Serializable data object RouteD3: AppTestKey

@OptIn(ExperimentalSerializationApi::class)
internal val savedStateConfiguration = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(AppNavKey::class) {
            subclassesOfSealed(AppTestKey.serializer())
        }
    }
}