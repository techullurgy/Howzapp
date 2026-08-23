package com.techullurgy.howzapp.feature.chats.domain.api.models.content

import com.techullurgy.howzapp.feature.users.domain.api.models.UserId

data class PollMessage(
    val question: String,
    val options: List<PollOption>,
    val multipleAnswers: Boolean
) : MessageContent

data class PollOption(
    val id: String,
    val title: String,
    val voters: List<UserId>
)
