package com.hongildong.map.data.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.hongildong.map.data.module.NetworkModule
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageRepository @Inject constructor(
    // 1. NetworkModule에서 @BaseRetrofit 이름표가 붙은 OkHttpClient를 주입받습니다.
    @NetworkModule.BaseRetrofit private val okHttpClient: OkHttpClient,
    @ApplicationContext private val context: Context
) {

    /**
     * Presigned URL을 사용해 S3에 이미지를 업로드합니다.
     * @param presignedUrl 서버로부터 받은 업로드용 URL
     * @param imageUri 기기에 저장된 이미지의 Uri
     * @return 성공 시 Result.success(Unit), 실패 시 Result.failure(Exception)
     */
    suspend fun uploadImageToS3(
        presignedUrl: String,
        imageUri: Uri
    ): Result<Unit> {
        // 네트워크 작업은 IO 스레드에서 수행
        return withContext(Dispatchers.IO) {
            try {
                // 2. Uri로부터 MIME 타입과 파일 크기를 가져옵니다.
                val mimeType = context.contentResolver.getType(imageUri) ?: "image/jpeg"
                val contentLength = getFileSize(imageUri)

                // 3. Uri의 InputStream에서 직접 데이터를 읽어 RequestBody를 생성합니다.
                val requestBody = object : RequestBody() {
                    override fun contentType() = mimeType.toMediaTypeOrNull()

                    // 파일 크기를 알 수 있다면 제공하는 것이 좋습니다.
                    override fun contentLength() = contentLength

                    override fun writeTo(sink: BufferedSink) {
                        context.contentResolver.openInputStream(imageUri)?.use { inputStream ->
                            sink.writeAll(inputStream.source())
                        } ?: throw IOException("Content resolver returned null stream")
                    }
                }

                // 4. PUT 요청을 빌드합니다.
                val request = Request.Builder()
                    .url(presignedUrl)
                    .put(requestBody)
                    .addHeader("Content-Type", mimeType)
                    .build()

                // 5. OkHttp 클라이언트로 요청을 동기적으로 실행 (withContext가 IO 스레드를 보장)
                val response = okHttpClient.newCall(request).execute()

                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.failure(IOException("S3 Upload Failed: ${response.message}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    /**
     * ContentResolver를 사용해 Uri로부터 파일 크기를 가져옵니다.
     */
    private fun getFileSize(uri: Uri): Long {
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                if (!cursor.isNull(sizeIndex)) {
                    return cursor.getLong(sizeIndex)
                }
            }
        }
        return -1 // 크기를 알 수 없음
    }
}