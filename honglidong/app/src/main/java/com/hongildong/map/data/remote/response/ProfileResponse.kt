package com.hongildong.map.data.remote.response

data class ProfileResponse(
    val email: String,
    val id: Int,
    val name: String,
    val nickname: String,
    val profilePic: String?
)