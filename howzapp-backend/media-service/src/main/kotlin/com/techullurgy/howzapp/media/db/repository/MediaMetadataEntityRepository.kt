package com.techullurgy.howzapp.media.db.repository

import com.techullurgy.howzapp.media.db.entities.MediaMetadataEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MediaMetadataEntityRepository : CoroutineCrudRepository<MediaMetadataEntity, String>