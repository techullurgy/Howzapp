package com.techullurgy.howzapp.common.core.pubsub

object PubSubConstants {

    const val USER_CHANNEL_PREFIX = "user:channel"
    const val PRESENCE_CHANNEL_PREFIX = "presence"
    const val LAST_SEEN_INVALIDATION_CHANNEL = "last_seen"

    const val USER_PRESENCE_EVENTS_CHANNEL = "system:presence:events"


    const val PRIVACY_KEY = "user:privacy"
    const val CONTACTS_PREFIX = "user:contacts:"
    const val PRESENCE_STATE_PREFIX = "user:presence:state:"
    const val LAST_SEEN_PREFIX = "user:presence:lastseen:"

    const val REDIS_MEDIA_PREFIX = "media:key:"
    const val UPLOAD_TOK_PREFIX = "media:upload_token:"
}