package com.hongildong.map.data.util


sealed class DefaultResponse<out T> {
    data class Success<out T>(
        val message: String,
        val data: T
    ) : DefaultResponse<T>()

    data class Error(
        val code: String? = null,
        val message: String
    ) : DefaultResponse<Nothing>()

}

