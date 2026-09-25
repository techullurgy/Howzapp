package com.techullurgy.howzapp.common.core.pubsub

fun userOutboxChannel(userId: String) = "user:outbox:$userId"