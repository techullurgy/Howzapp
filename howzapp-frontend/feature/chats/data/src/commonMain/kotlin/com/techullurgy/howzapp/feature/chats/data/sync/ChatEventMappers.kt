package com.techullurgy.howzapp.feature.chats.data.sync

import com.techullurgy.howzapp.common.websocket.ChatBasedClientToServer
import com.techullurgy.howzapp.common.websocket.ChatBasedServerToClient
import com.techullurgy.howzapp.feature.chats.domain.api.events.ChatEvent


internal fun ChatBasedServerToClient.toChatEventIncoming(): ChatEvent.Incoming { TODO() }
internal fun ChatEvent.Outgoing.toChatBasedClientToServer(): ChatBasedClientToServer { TODO() }