package com.hongildong.map.data.util

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 서버 API의 공통 응답 포맷을 위한 제네릭 데이터 클래스
 * @param T API 응답의 'result' 필드에 해당하는 데이터 타입
 */
@JsonClass(generateAdapter = true)
data class ApiResponse<T> (
    @field:Json(name = "isSuccess")
    val isSuccess: Boolean,

    @field:Json(name = "code")
    val code: String,

    @field:Json(name = "message")
    val message: String,

    @field:Json(name = "result")
    val result: T?
)