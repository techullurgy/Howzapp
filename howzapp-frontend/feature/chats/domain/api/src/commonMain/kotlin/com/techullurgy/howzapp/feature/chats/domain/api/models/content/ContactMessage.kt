package com.techullurgy.howzapp.feature.chats.domain.api.models.content

data class ContactMessage(
    val contact: ContactCard
) : MessageContent

data class ContactCard(
    val name: String,
    val phoneNumber: String,
    val organization: String?
)