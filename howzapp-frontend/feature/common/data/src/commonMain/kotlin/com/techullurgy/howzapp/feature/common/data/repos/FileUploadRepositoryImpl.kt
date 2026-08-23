package com.techullurgy.howzapp.feature.common.data.repos

import com.techullurgy.howzapp.feature.common.db.dao.FileUploadDao
import com.techullurgy.howzapp.feature.common.domain.api.repos.FileUploadRepository
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Singleton

@Singleton(binds = [FileUploadRepository::class])
internal class FileUploadRepositoryImpl(
    @Provided private val fileUploadDao: FileUploadDao
): FileUploadRepository {
}