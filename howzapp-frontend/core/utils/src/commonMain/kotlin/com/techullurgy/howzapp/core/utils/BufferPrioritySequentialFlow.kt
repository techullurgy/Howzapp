package com.techullurgy.howzapp.core.utils

// commonMain
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch

fun <T, K> Flow<Iterable<T>>.bufferPrioritySequential(
    keySelector: (T) -> K,
    comparator: Comparator<T>
): Flow<T> = channelFlow {
    val queue = PriorityQueue(comparator, keySelector)
    val signal = Channel<Unit>(Channel.UNLIMITED)

    // Upstream: enqueues items safely
    val producerJob = launch {
        try {
            collect { currentList ->
                var addedAny = false
                for (item in currentList) {
                    if (queue.offerIfAbsent(item)) {
                        addedAny = true
                    }
                }
                if (addedAny) {
                    signal.trySend(Unit)
                }
            }
        } finally {
            signal.close()
        }
    }

    // Downstream: polls items safely
    while (true) {
        val nextItem = queue.poll()

        if (nextItem != null) {
            send(nextItem)
        } else {
            val hasMore = signal.receiveCatching().isSuccess
            if (!hasMore) {
                val remaining = queue.poll()
                if (remaining != null) {
                    send(remaining)
                } else {
                    break
                }
            }
        }
    }

    producerJob.join()
}.buffer(Channel.RENDEZVOUS)