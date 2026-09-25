package com.techullurgy.howzapp.core.utils

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class PriorityQueue<T, K>(
    private val comparator: Comparator<T>,
    private val keySelector: (T) -> K
) {
    private val heap = mutableListOf<T>()
    private val seenKeys = mutableSetOf<K>()
    private val mutex = Mutex()

    /**
     * Atomically checks for duplicates and enqueues the item.
     * Returns true if newly added, false if already seen.
     */
    suspend fun offerIfAbsent(element: T): Boolean = mutex.withLock {
        val key = keySelector(element)
        if (seenKeys.add(key)) {
            heap.add(element)
            siftUp(heap.size - 1)
            true
        } else {
            false
        }
    }

    suspend fun poll(): T? = mutex.withLock {
        if (heap.isEmpty()) return null
        val result = heap[0]
        val lastItem = heap.removeAt(heap.size - 1)
        if (heap.isNotEmpty()) {
            heap[0] = lastItem
            siftDown(0)
        }
        return result
    }

    private fun siftUp(index: Int) {
        var current = index
        while (current > 0) {
            val parent = (current - 1) / 2
            if (comparator.compare(heap[current], heap[parent]) < 0) {
                heap.swap(current, parent)
                current = parent
            } else {
                break
            }
        }
    }

    private fun siftDown(index: Int) {
        var current = index
        val half = heap.size / 2
        while (current < half) {
            var child = 2 * current + 1
            val right = child + 1
            if (right < heap.size && comparator.compare(heap[right], heap[child]) < 0) {
                child = right
            }
            if (comparator.compare(heap[current], heap[child]) <= 0) {
                break
            }
            heap.swap(current, child)
            current = child
        }
    }

    private fun <E> MutableList<E>.swap(i: Int, j: Int) {
        val temp = this[i]
        this[i] = this[j]
        this[j] = temp
    }
}