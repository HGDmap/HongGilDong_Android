package com.hongildong.map.data.repository

import com.hongildong.map.data.remote.request.SigninRequest
import com.hongildong.map.data.remote.request.SignupRequest
import com.hongildong.map.data.remote.response.SigninResponse
import com.hongildong.map.data.util.DefaultResponse

interface AuthRepository {
    suspend fun test(accessToken: String) : DefaultResponse<String>

    suspend fun signup(body: SignupRequest) : DefaultResponse<SigninResponse?>
    suspend fun signin(body: SigninRequest) : DefaultResponse<SigninResponse?>
}