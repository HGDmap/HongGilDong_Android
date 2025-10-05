package com.hongildong.map.data.util


import com.hongildong.map.data.util.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

suspend inline fun <reified T> safeApiCall(
    crossinline apiCall: suspend () -> ApiResponse<T>
): DefaultResponse<T> = withContext(Dispatchers.IO) {
    try {
        //api 호출
        val rawResponse = apiCall()

        // 성공시 응답값 저장
        if (rawResponse.isSuccess) {
            DefaultResponse.Success(
                message = rawResponse.message,
                data = rawResponse.result ?: throw IllegalStateException("Success response should contain data")
            )
        } else {
            DefaultResponse.Error(
                code = rawResponse.code,
                message = rawResponse.message ?: "알 수 없는 오류"
            )
        }

    } catch (http: HttpException) {
        // 상태 코드 확인
        val code = http.code()
        val parsed = ErrorParser.parseHttpException(http)
        DefaultResponse.Error(
            code = parsed.code,
            message = parsed.message,
        )
    } catch (e: Exception) {
        // api 요청 실패
        DefaultResponse.Error(message = "네트워크 오류: ${e.localizedMessage}")
    }
}
