package com.techullurgy.howzapp.feature.common.db.dao

import androidx.room3.Query
import androidx.room3.Upsert
import com.techullurgy.howzapp.feature.common.db.entities.FileUploadEntity

interface FileUploadDao {

    @Query("SELECT * FROM FileUploadEntity WHERE uploadId = :uploadId")
    suspend fun findByUploadId(uploadId: String): FileUploadEntity?

    @Query("DELETE FROM FileUploadEntity WHERE uploadId = :uploadId")
    suspend fun deleteUpload(uploadId: String)

    @Upsert
    suspend fun upsertNewUpload(entity: FileUploadEntity)
}