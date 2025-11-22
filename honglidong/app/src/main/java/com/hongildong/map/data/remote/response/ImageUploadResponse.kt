package com.hongildong.map.data.remote.response

data class ImageUploadResponse(
    val imageURL: String,
    val presignedURL: String
)