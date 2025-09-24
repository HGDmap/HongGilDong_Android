package com.hongildong.map.data.remote.api

import com.hongildong.map.data.remote.request.SigninRequest
import com.hongildong.map.data.util.ApiResponse
import com.hongildong.map.data.remote.response.SigninResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {

    // 실험 api - 정확한 건 회의하면서 작성해야될듯
    @POST("auth/random")
    suspend fun test(
        @Header("Authorization") accessToken: String
    ): ApiResponse<String>

    @POST("auth/signin")
    suspend fun signin(
        @Header("Authorization") accessToken: String,
        @Body body: SigninRequest
    ): ApiResponse<SigninResponse?>
}