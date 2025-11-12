package com.hongildong.map.data.remote.response

data class PhotoResponse(
    val continuationToken: String,
    val imageList: List<String>,
    val isFirst: Boolean,
    val isLast: Boolean,
    val pageSize: Int
)