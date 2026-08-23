package com.techullurgy.howzapp.root.navigation.temp.status

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds

data class StatusSegment(val id: String, val mediaColor: Color, val duration: Int)
data class UserStatus(val userId: String, val userName: String, val segments: List<StatusSegment>)

@Composable
fun StatusViewerScreen(
    userStatuses: List<UserStatus>,
    onAllStoriesCompleted: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val parentPagerState = rememberPagerState { userStatuses.size }

    HorizontalPager(
        state = parentPagerState,
        modifier = Modifier.fillMaxSize(),
        key = { index -> userStatuses[index].userId }
    ) { userIndex ->
        val currentUserStatus = userStatuses[userIndex]

        val isPageActive = parentPagerState.currentPage == userIndex
    }
}

@Composable
private fun UserStoryContent(
    userStatus: UserStatus,
    isActive: Boolean,
    onUserCompleted: () -> Unit,
    onUserPrevious: () -> Unit
) {
    var currentSegmentIndex by remember(userStatus) { mutableStateOf(0) }
    var isPaused by remember { mutableStateOf(false) }

    LaunchedEffect(isActive) {
        if(!isActive) currentSegmentIndex = 0
    }

    Box(
        Modifier.fillMaxSize().background(Color.Black)
    ) {
        // Media Layer
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(userStatus) {
                    detectTapGestures(
                        onPress = {
                            isPaused = true
                            tryAwaitRelease()
                            isPaused = false
                        },
                        onTap = { offset ->
                            val width = size.width
                            if(offset.x < width * 0.3f) {
                                // Left Tap: Previous Segment or Previous User
                                if(currentSegmentIndex > 0) {
                                    currentSegmentIndex--
                                } else {
                                    onUserPrevious()
                                }
                            } else if(offset.x > width * (1f - 0.3f)) {
                                // Right Tap: Next Segment or Next User
                                if(currentSegmentIndex < userStatus.segments.lastIndex) {
                                    currentSegmentIndex++
                                } else {
                                    onUserCompleted()
                                }
                            }
                        }
                    )
                }
        ) {
            Box(
                Modifier.fillMaxSize().background(userStatus.segments[currentSegmentIndex].mediaColor)
            )
        }

        // Top Progress Bars
        Column(
            Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 16.dp, start = 8.dp, end = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                userStatus.segments.forEachIndexed { index, segment ->
                    StoryProgressBar(
                        duration = segment.duration,
                        modifier = Modifier.weight(1f),
                        index = index,
                        currentIndex = currentSegmentIndex,
                        isActive = isActive,
                        isPaused = isPaused,
                        onSegmentFinished = {
                            if(currentSegmentIndex < userStatus.segments.size) {
                                currentSegmentIndex++
                            } else {
                                onUserCompleted()
                            }
                        }
                    )
                }
            }

            BasicText(
                text = userStatus.userName,
                modifier = Modifier
                    .padding(top = 12.dp, start = 4.dp, )
            )
        }
    }
}

@Composable
private fun StoryProgressBar(
    duration: Int,
    index: Int,
    currentIndex: Int,
    isActive: Boolean,
    isPaused: Boolean,
    onSegmentFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    var progress by remember(currentIndex) {
        mutableStateOf(if(index < currentIndex) 1f else 0f)
    }

    LaunchedEffect(currentIndex, isActive, isPaused) {
        if(isActive && index == currentIndex && !isPaused) {
            val startTime = Clock.System.now().toEpochMilliseconds()
            val startProgress = progress
            val remainingTime = ((1f - startProgress) * duration).toLong()

            while (Clock.System.now().toEpochMilliseconds() - startTime < remainingTime) {
                val elapsed = Clock.System.now().toEpochMilliseconds() - startTime
                progress = startProgress + (elapsed.toFloat() / duration)
                delay(16.milliseconds)
            }
            progress = 1f
            onSegmentFinished()
        }
    }

//    LinearProgressIndicator(
//        progress = { progress },
//        color = Color.White,
//        trackColor = Color.White.copy(alpha = 0.6f),
//        modifier = modifier.height(3.dp).clip(RoundedCornerShape(2.dp))
//    )
}