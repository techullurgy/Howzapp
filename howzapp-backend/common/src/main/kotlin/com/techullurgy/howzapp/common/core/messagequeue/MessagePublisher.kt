package com.techullurgy.howzapp.common.core.messagequeue

interface MessagePublisher<in T> {
    /**
     * Publish the message on provided binding
     */
    fun publish(binding: String, message: T)
}