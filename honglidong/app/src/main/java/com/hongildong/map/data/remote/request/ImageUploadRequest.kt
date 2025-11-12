package com.hongildong.map.data.remote.request

data class ImageUploadRequest(
    val facilityId: Int,
    val fileNames: List<String>
)