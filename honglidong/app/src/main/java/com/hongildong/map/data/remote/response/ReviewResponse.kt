package com.hongildong.map.data.remote.response

import com.hongildong.map.data.entity.ReviewInfo

data class ReviewResponse(
    val isFirst: Boolean,
    val isLast: Boolean,
    val reviewList: List<ReviewInfo>,
    val size: Int,
    val totalElements: Int,
    val totalPages: Int
)