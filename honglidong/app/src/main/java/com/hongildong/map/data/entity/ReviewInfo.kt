package com.hongildong.map.data.entity

data class ReviewInfo(
    val content: String,
    val createdAt: String,
    val id: Int,
    val isLiked: Boolean,
    val photoList: List<String?>,
    val updatedAt: String,
    val writerId: Int,
    val writerNickname: String,
    val writerProfilePic: String? = null,
    val likeCnt: Int? = null
)