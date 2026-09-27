package com.techullurgy.howzapp.feature.users.domain.api.usecases

import com.techullurgy.howzapp.feature.users.domain.api.models.User
import com.techullurgy.howzapp.feature.users.domain.api.models.UserId

interface ObtainUserFromUserIdUseCase {
    suspend operator fun invoke(userId: UserId): User?
}