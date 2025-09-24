package com.hongildong.map.data.repository

import android.content.Context
import com.hongildong.map.data.remote.api.AuthService
import com.hongildong.map.data.remote.request.SigninRequest
import com.hongildong.map.data.remote.response.SigninResponse
import com.hongildong.map.data.util.DefaultResponse
import com.hongildong.map.data.util.safeApiCall
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api : AuthService
) : AuthRepository {

    override suspend fun test(
        accessToken: String
    ): DefaultResponse<String> {
        return safeApiCall { api.test(accessToken) }
    }

    override suspend fun signin(
        accessToken: String,
        body: SigninRequest
    ): DefaultResponse<SigninResponse?> {
        return safeApiCall { api.signin(accessToken, body) }
    }
}