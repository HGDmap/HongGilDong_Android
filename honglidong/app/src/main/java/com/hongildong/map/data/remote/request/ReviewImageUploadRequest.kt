package com.hongildong.map.data.remote.request

data class ReviewImageUploadRequest(
    val facilityId: Int,
    val fileNames: List<String>
)