package com.techullurgy.howzapp.media.services

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.time.Duration

@Service
class S3PresignerService(
    private val s3Presigner: S3Presigner,
    @Value($$"${aws.s3.bucket-name}") private val bucketName: String,
    @Value($$"${aws.s3.cdn-domain}") private val cdnDomain: String
) {
    fun generatePresignedUploadUrl(objectKey: String, mimeType: String): Pair<String, String> {
        val putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(objectKey)
            .contentType(mimeType)
            .build()

        val presignRequest = PutObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(15))
            .putObjectRequest(putObjectRequest)
            .build()

        val presignedUrl = s3Presigner.presignPutObject(presignRequest).url().toString()
        val cdnOriginalUrl = "$cdnDomain/$objectKey"

        return Pair(presignedUrl, cdnOriginalUrl)
    }
}