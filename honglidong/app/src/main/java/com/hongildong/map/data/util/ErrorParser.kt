package com.hongildong.map.data.util

import com.google.gson.Gson
import com.hongildong.map.data.util.ApiResponse
import retrofit2.HttpException

object ErrorParser {
    private val gson = Gson()

    fun parseHttpException(exception: HttpException): DefaultResponse.Error {
        return try {
            val errorBody = exception.response()?.errorBody()?.string()
            if (errorBody.isNullOrEmpty()) {
                DefaultResponse.Error(message = "서버 오류 발생")
            } else {
                val json = gson.fromJson(errorBody, ApiResponse::class.java)
                DefaultResponse.Error(
                    code = json.code,
                    message = json.message ?: "서버 오류 발생"
                )
            }
        } catch (e: Exception) {
            DefaultResponse.Error(message = "에러 파싱 실패: ${e.localizedMessage}")
        }
    }
}
