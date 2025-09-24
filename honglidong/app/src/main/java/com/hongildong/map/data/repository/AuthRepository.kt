package com.hongildong.map.data.repository

import com.hongildong.map.data.remote.request.SigninRequest
import com.hongildong.map.data.remote.response.SigninResponse
import com.hongildong.map.data.util.DefaultResponse

interface AuthRepository {
    suspend fun test(accessToken: String) : DefaultResponse<String>
    suspend fun signin(accessToken: String, body: SigninRequest) : DefaultResponse<SigninResponse?>
}