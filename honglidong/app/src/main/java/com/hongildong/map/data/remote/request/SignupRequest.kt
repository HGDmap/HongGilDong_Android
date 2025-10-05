package com.hongildong.map.data.remote.request

data class SignupRequest(
    val email: String,
    val fullName: String,
    val nickname: String,
    val password: String
)