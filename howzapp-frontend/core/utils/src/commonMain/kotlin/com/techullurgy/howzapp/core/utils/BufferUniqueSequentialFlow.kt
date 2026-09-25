package com.techullurgy.howzapp.core.utils

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch

fun <T, K> Flow<Iterable<T>>.bufferUniqueSequential(
    keySelector: (T) -> K
): Flow<T> = channelFlow {
    // Tracks keys already pushed to avoid duplicates across Room re-queries
    val seenKeys = mutableSetOf<K>()

    // Internal queue acting as the intermediate buffer
    val workQueue = Channel<T>(Channel.UNLIMITED)

    // Upstream collector: parses Room updates and feeds new items into the buffer
    val producerJob = launch {
        try {
            collect { currentList ->
                for (item in currentList) {
                    val key = keySelector(item)
                    if (seenKeys.add(key)) {
                        workQueue.send(item)
                    }
                }
            }
        } finally {
            workQueue.close()
        }
    }

    // Downstream feeder: reads from the buffer one-by-one as downstream is ready
    for (item in workQueue) {
        send(item) // Suspends if downstream collector is busy
    }

    producerJob.join()
}
