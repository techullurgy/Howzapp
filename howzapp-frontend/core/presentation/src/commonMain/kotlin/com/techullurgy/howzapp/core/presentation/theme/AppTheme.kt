package com.techullurgy.howzapp.core.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

interface AppThemeColors {
    val backgroundColor: Color
    val contentColor: Color
    val onlineIndicatorColor: Color
    val topBarBackgroundColor: Color
    val messageCardYouColor: Color
    val messageCardYouTintContentColor: Color
    val messageCardYouMutedContentColor: Color
    val messageCardOtherColor: Color
    val messageCardOtherTintContentColor: Color
    val messageCardOtherMutedContentColor: Color
    val readTickColor: Color
    val separatorBackgroundColor: Color
    val separatorTextColor: Color
    val systemMessageBackgroundColor: Color
    val systemMessageTextColor: Color
}

private object DarkAppThemeColors : AppThemeColors {
    var hueProvider: HueProvider = DefaultThemeHueProvider

    override val backgroundColor get() = Color.hsv(hueProvider.primaryHue, .91f, .15f)
    override val topBarBackgroundColor get() = Color.hsv(hueProvider.primaryHue, .93f, .52f)
    override val contentColor get() = Color.White
    override val messageCardYouColor get() = Color.hsv(hueProvider.primaryHue, .93f, .43f)
    override val messageCardYouTintContentColor get() = Color.hsv(hueProvider.primaryHue, .51f, .92f)
    override val messageCardYouMutedContentColor get() = Color.hsv(hueProvider.primaryHue, .14f, .48f)
    override val messageCardOtherColor get() = Color.hsv(hueProvider.secondaryHue, .93f, .35f)
    override val messageCardOtherTintContentColor get() = Color.hsv(hueProvider.secondaryHue, .63f, .90f)
    override val messageCardOtherMutedContentColor get() = Color.hsv(hueProvider.primaryHue, .13f, .47f)
    override val readTickColor get() = Color.hsv(hueProvider.readTickHue, .59f, .98f)
    override val separatorTextColor get() = Color.hsv(hueProvider.tertiaryHue, .64f, .88f)
    override val separatorBackgroundColor get() = Color.hsv(hueProvider.tertiaryHue, .92f, .45f)
    override val systemMessageBackgroundColor get() = Color.hsv(hueProvider.systemHue,.95f,.59f)
    override val systemMessageTextColor get() = Color.hsv(hueProvider.systemHue,.83f,.95f)
    override val onlineIndicatorColor get() = Color.hsv(LightAppThemeColors.hueProvider.onlineIndicatorHue,.83f,.80f)
}

private object LightAppThemeColors : AppThemeColors {
    var hueProvider: HueProvider = DefaultThemeHueProvider

    override val backgroundColor get() = Color.hsv(hueProvider.primaryHue, .09f, .91f)
    override val topBarBackgroundColor get() = Color.hsv(hueProvider.primaryHue, .67f, .94f)
    override val contentColor get() = Color.Black
    override val messageCardYouColor get() = Color.hsv(hueProvider.primaryHue, .49f, .95f)
    override val messageCardYouTintContentColor get() = Color.hsv(hueProvider.primaryHue, .83f, .67f)
    override val messageCardYouMutedContentColor get() = Color.hsv(DarkAppThemeColors.hueProvider.primaryHue, .07f, .35f)
    override val messageCardOtherColor get() = Color.hsv(hueProvider.secondaryHue, .46f, .94f)
    override val messageCardOtherTintContentColor get() = Color.hsv(hueProvider.secondaryHue, .92f, .66f)
    override val messageCardOtherMutedContentColor get() = Color.hsv(DarkAppThemeColors.hueProvider.secondaryHue, .05f, .42f)
    override val readTickColor get() = Color.hsv(hueProvider.readTickHue, .93f, .9f)
    override val separatorTextColor get() = Color.hsv(hueProvider.tertiaryHue, .92f, .45f)
    override val separatorBackgroundColor get() = Color.hsv(hueProvider.tertiaryHue, .64f, .88f)
    override val systemMessageBackgroundColor get() = Color.hsv(hueProvider.systemHue,.83f,.95f)
    override val systemMessageTextColor get() = Color.hsv(hueProvider.systemHue,.95f,.59f)
    override val onlineIndicatorColor get() = Color.hsv(hueProvider.onlineIndicatorHue,.83f,.80f)
}

object AppThemeProvider {
    val colors : AppThemeColors
        @Composable get() = LocalAppThemeColors.current
}

private val LocalAppThemeColors = staticCompositionLocalOf<AppThemeColors> {
    LightAppThemeColors.apply { hueProvider = DefaultThemeHueProvider }
}


private interface HueProvider {
    val primaryHue: Float
    val secondaryHue: Float
    val tertiaryHue: Float
    val systemHue: Float
    val errorHue: Float
    val readTickHue: Float
    val onlineIndicatorHue: Float
}

private object DefaultThemeHueProvider: HueProvider {
    override val primaryHue: Float = 271f
    override val secondaryHue: Float = 202f
    override val tertiaryHue: Float = 309f
    override val systemHue: Float = 33f
    override val errorHue: Float = 10f
    override val readTickHue: Float = 243f
    override val onlineIndicatorHue: Float = 125f
}


@Composable
fun AppTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val appTheme = if(isDarkTheme) {
        DarkAppThemeColors.apply { hueProvider = DefaultThemeHueProvider }
    } else {
        LightAppThemeColors.apply { hueProvider = DefaultThemeHueProvider }
    }
    CompositionLocalProvider(
        LocalAppThemeColors provides appTheme,
        LocalContentColor provides appTheme.contentColor,
        content = content
    )
}