package com.techullurgy.howzapp.feature.common.db.entities

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.techullurgy.howzapp.feature.common.db.models.FileUploadPurposeStored
import com.techullurgy.howzapp.feature.common.db.models.FileUploadStatusStored

@Entity
data class FileUploadEntity(
    @PrimaryKey val id: String,
    val uploadId: String,
    val serverKey: String?,
    val purpose: FileUploadPurposeStored,
    val status: FileUploadStatusStored
)