package com.hongildong.map.data.remote.request

data class ReviewUpdateRequest(
    val content: String,
    val photoList: List<String>
)