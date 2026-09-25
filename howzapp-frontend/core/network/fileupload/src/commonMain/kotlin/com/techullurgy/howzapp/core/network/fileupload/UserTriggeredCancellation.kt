package com.techullurgy.howzapp.core.network.fileupload

import kotlinx.coroutines.CancellationException


class UserTriggeredCancellation: CancellationException("User triggered cancellation")