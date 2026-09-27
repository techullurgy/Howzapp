package com.techullurgy.howzapp.feature.chats.presentation.impl.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.techullurgy.howzapp.core.presentation.components.AppIcon
import com.techullurgy.howzapp.core.presentation.components.AppText
import com.techullurgy.howzapp.core.presentation.theme.AppThemeProvider
import com.techullurgy.howzapp.core.presentation.theme.icons.play
import com.techullurgy.howzapp.feature.chats.domain.api.models.content.VoiceMessage
import com.techullurgy.howzapp.feature.chats.presentation.impl.models.OwnerMetadata
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Composable
internal fun VoiceMessageUiItem(
    item: VoiceMessage,
    owner: OwnerMetadata,
    modifier: Modifier = Modifier
) {
    require(owner != OwnerMetadata.System)

    val mediaColor = when(owner) {
        is OwnerMetadata.Person.Other -> AppThemeProvider.colors.messageCardOtherTintContentColor
        is OwnerMetadata.Person.You -> AppThemeProvider.colors.messageCardYouTintContentColor
    }

    val filledWaveColor = when(owner) {
        is OwnerMetadata.Person.Other -> AppThemeProvider.colors.messageCardOtherColor
        is OwnerMetadata.Person.You -> AppThemeProvider.colors.messageCardYouColor
    }

    Layout(
        content = {
            // Play/Pause Icon
            AppIcon(
                imageVector = play,
                contentDescription = "Play/Pause",
                tint = mediaColor,
                modifier = Modifier.size(48.dp)
            )

            WaveLayout {
                // Waves
                WavePattern(
                    waves = emptyList(), // item.waveForm,
                    elapsed = 40.seconds,
                    filledWaveColor = filledWaveColor,
                    unfilledWaveColor = mediaColor,
                    modifier = Modifier.height(40.dp)
                )

                // Elapsed Time(When Playing) / Duration // Aligned Right
                AppText(
                    text = "04:55/${item.durationSeconds}",
                    style = TextStyle(
                        color = mediaColor,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        },
        modifier = modifier
    ) { measurables, constraints ->
        val placeables = measurables.map { measurable -> measurable.measure(constraints) }
        val totalWidth = placeables.sumOf { it.width }
        val totalHeight = placeables.maxOf { it.height }
        layout(totalWidth, totalHeight) {
            var placeX = 0
            placeables.forEach { placeable ->
                placeable.place(placeX, 0)
                placeX += placeable.width
            }
        }
    }
}

@Composable
private fun WaveLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val maxConstraints = constraints.copy(minWidth = 0, maxWidth = 100.dp.roundToPx())

        val wavePatternPlaceable = measurables[0].measure(maxConstraints)
        val durationsPlaceable = measurables[1].measure(maxConstraints)

        val totalWidth = maxOf(wavePatternPlaceable.width, durationsPlaceable.width)
        val totalHeight = wavePatternPlaceable.height + durationsPlaceable.height

        layout(totalWidth, totalHeight) {
            wavePatternPlaceable.place(0, 0)
            durationsPlaceable.place(totalWidth - durationsPlaceable.width, wavePatternPlaceable.height)
        }
    }
}

@Composable
private fun WavePattern(
    waves: List<Pair<Long, Float>>,
    elapsed: Duration,
    filledWaveColor: Color,
    unfilledWaveColor: Color,
    modifier: Modifier = Modifier
) {
    val updatedElapsedTime by rememberUpdatedState(elapsed)

    val upToIndex by produceState(-1, waves) {
        val waveTimestamps = waves.map { it.first }
        val currentElapsedTimestamp = updatedElapsedTime.inWholeMilliseconds

        value = waveTimestamps.indexOfFirst { t -> t > currentElapsedTimestamp }
            .let {
                if(it == -1) waves.lastIndex
                else it - 1
            }
    }

    Canvas(
        modifier = modifier.fillMaxSize()
    ) {
        val eachLength = size.width / waves.size

        waves.forEachIndexed { index, (_, volume) ->
            val stripSize = Size(eachLength, size.height * volume)
            val topLeft = Offset(
                x = index * eachLength,
                y = (size.height - stripSize.height) * .5f
            )

            val isFilled = index <= upToIndex

            // Unfilled Strips
            drawRoundRect(
                color = unfilledWaveColor,
                topLeft = topLeft,
                size = stripSize,
                cornerRadius = CornerRadius(eachLength/2)
            )

            if(isFilled) {
                // Filled Strips
                drawRoundRect(
                    color = filledWaveColor,
                    topLeft = topLeft,
                    size = stripSize,
                    cornerRadius = CornerRadius(eachLength/2)
                )
            }

            // Outline / Border on Strips
            drawRoundRect(
                color = unfilledWaveColor,
                style = Stroke(eachLength * 0.2f, cap = StrokeCap.Round),
                topLeft = topLeft,
                size = stripSize,
                cornerRadius = CornerRadius(eachLength/2)
            )
        }
    }
}