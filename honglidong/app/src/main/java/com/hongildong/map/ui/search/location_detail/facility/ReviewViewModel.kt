package com.hongildong.map.ui.search.location_detail.facility

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hongildong.map.data.remote.request.ImageUploadRequest
import com.hongildong.map.data.remote.request.ReviewUpdateRequest
import com.hongildong.map.data.remote.response.ImageUploadResponse
import com.hongildong.map.data.repository.ReviewRepository
import com.hongildong.map.data.util.DefaultResponse
import com.hongildong.map.data.util.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val imageRepository: ImageRepository,
    @ApplicationContext private val context: Context
): ViewModel() {
    // 로컬 이미지 uri 리스트
    private val _selectedImageUris = MutableStateFlow<List<Uri>>(emptyList())
    val selectedImageUris = _selectedImageUris.asStateFlow()

    /*private val _uploadState = MutableStateFlow<ImageUploadState>(ImageUploadState.Idle)
    val uploadState = _uploadState.asStateFlow()*/

    private val TAG = this.javaClass.simpleName
    private val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    fun getToken(): String? {
        return sharedPreferences.getString("access_token", null)
    }

    fun addImage(uris: List<Uri>) {
        viewModelScope.launch {
            _selectedImageUris.value = uris
        }
    }

    // 리뷰 등록하기
    fun createReview(
        facilityId: Int,
        content: String,
    ) {
        viewModelScope.launch {
            val token = getToken() ?: return@launch

            if (_selectedImageUris.value.isEmpty()) {
                // 이미지가 없는 리뷰 등록
                val body = ReviewUpdateRequest(
                    content = content,
                    photoList = emptyList()
                )
                val response = reviewRepository.createReview(token, facilityId, body)
                when (response) {
                    is DefaultResponse.Success -> {
                        // 성공 처리
                        Log.d(TAG, "create review 응답 성공: $response")
                    }
                    is DefaultResponse.Error -> {
                        Log.d(TAG, "create review 응답 실패: $response")
                    }
                }
            } else {
                // 이미지 있는 리뷰 등록
                // uri 리스트로 presigned url 받아오기
                //getPresignedUrl(facilityId)

                val fileNames = _selectedImageUris.value.map { uri -> imageRepository.getFileName(uri) ?: "" }
                val imageUploadRequest = ImageUploadRequest(
                    facilityId = facilityId,
                    fileNames = fileNames
                )
                Log.d(TAG, "create presigned url request body: $imageUploadRequest")
                val presignedUrlResponse = reviewRepository.createPresignedUrl(token, imageUploadRequest)
                when (presignedUrlResponse) {
                    is DefaultResponse.Success -> {
                        Log.d(TAG, "create presigned url 응답 성공: $presignedUrlResponse")
                        _serverUrls.value = _selectedImageUris.value.zip(presignedUrlResponse.data)
                    }
                    is DefaultResponse.Error -> {
                        Log.d(TAG, "create presigned url 응답 실패: $presignedUrlResponse")
                        return@launch
                    }
                }

                // s3에 이미지 업로드
                //uploadImageToS3()

                _serverUrls.value.forEach {
                    val imageUri = it.first
                    val presignedUrl = it.second.presignedURL
                    try {
                        imageRepository.uploadImageToS3(presignedUrl, imageUri).onFailure {
                            return@forEach // 실패 시 Exception을 발생시켜 catch 블록으로 보냄
                        }
                        //_uploadState.value = ImageUploadState.Success
                        Log.d(TAG, "s3 이미지 업로드 성공")
                        _selectedImageUris.value = emptyList() // 성공 후 목록 비우기
                    } catch (e: Exception) {
                        // 업로드 과정 중 하나라도 실패하면
                        Log.e(TAG, "업로드 실패", e)
                        return@forEach
                        //_uploadState.value = ImageUploadState.Error(e.message ?: "업로드에 실패했습니다.")
                    }
                }

                // 업로드 후 imageUrl로 api 요청
                val body = ReviewUpdateRequest(
                    content = content,
                    photoList = _serverUrls.value.map { (uri, response) -> response.imageURL }
                )

                val response = reviewRepository.createReview(token, facilityId, body)
                when (response) {
                    is DefaultResponse.Success -> {
                        // 성공 처리
                        Log.d(TAG, "create review 응답 성공: $response")
                    }
                    is DefaultResponse.Error -> {
                        Log.d(TAG, "create review 응답 실패: $response")
                    }
                }
            }
        }
    }

    private val _serverUrls = MutableStateFlow<List<Pair<Uri, ImageUploadResponse>>>(emptyList())
    val serverUrls = _serverUrls.asStateFlow()

    // presigned url 받아오기
    fun getPresignedUrl(facilityId: Int) {
        viewModelScope.launch {
            val token = getToken() ?: return@launch

            val fileNames = _selectedImageUris.value.map { uri -> imageRepository.getFileName(uri) ?: "" }
            val body = ImageUploadRequest(
                facilityId = facilityId,
                fileNames = fileNames
            )
            Log.d(TAG, "create presigned url request body: $body")
            val response = reviewRepository.createPresignedUrl(token, body)
            when (response) {
                is DefaultResponse.Success -> {
                    Log.d(TAG, "create presigned url 응답 성공: $response")
                    _serverUrls.value = _selectedImageUris.value.zip(response.data)
                }
                is DefaultResponse.Error -> {
                    Log.d(TAG, "create presigned url 응답 실패: $response")
                    return@launch
                }
            }
        }
    }

    // presigned url에 이미지 등록
    fun uploadImageToS3() {
        viewModelScope.launch {
            _serverUrls.value.forEach {
                val imageUri = it.first
                val presignedUrl = it.second.presignedURL
                try {
                    imageRepository.uploadImageToS3(presignedUrl, imageUri).onFailure {
                        return@forEach // 실패 시 Exception을 발생시켜 catch 블록으로 보냄
                    }
                    //_uploadState.value = ImageUploadState.Success
                    Log.d(TAG, "s3 이미지 업로드 성공")
                    _selectedImageUris.value = emptyList() // 성공 후 목록 비우기
                } catch (e: Exception) {
                    // 업로드 과정 중 하나라도 실패하면
                    Log.e(TAG, "업로드 실패", e)
                    //_uploadState.value = ImageUploadState.Error(e.message ?: "업로드에 실패했습니다.")
                }
            }
        }
    }

    // 이미지

    /*


        // 4. UI가 구독할 업로드 상태


        fun uploadImages() {
            viewModelScope.launch {
                _uploadState.value = ImageUploadState.Loading
                val uris = _selectedImageUris.value
                if (uris.isEmpty()) {
                    _uploadState.value = ImageUploadState.Error("선택된 이미지가 없습니다.")
                    return@launch
                }

                try {
                    // 5. 선택된 모든 이미지에 대해 반복
                    uris.forEach { uri ->
                        // 6. Uri에서 파일명을 추출합니다. (서버에 요청 시 필요)
                        val fileName = getFileName(uri)
                        if (fileName == null) {
                            Log.e("ImageUploadViewModel", "파일명 추출 실패: $uri")
                            return@forEach // 다음 이미지로
                        }

                        // 7. (가정) 자체 서버에 파일명을 보내 Presigned URL을 받아옵니다.
                        // val presignedUrlResponse = yourServerRepository.getPresignedUrl(fileName)
                        // if (!presignedUrlResponse.isSuccess) {
                        //    throw Exception("Presigned URL 발급 실패: $fileName")
                        // }
                        // val presignedUrl = presignedUrlResponse.result.url

                        // --- 테스트를 위한 임시 URL ---
                        val presignedUrl = "https://your-s3-presigned-url.com/$fileName"
                        Log.d("ImageUploadViewModel", "업로드 시도: $fileName -> $presignedUrl")
                        // --- 테스트 ---

                        // 8. StorageRepository를 사용해 S3에 실제 파일 업로드
                        imageRepository.uploadImageToS3(presignedUrl, uri).onFailure {
                            throw it // 실패 시 Exception을 발생시켜 catch 블록으로 보냄
                        }
                    }

                    // 모든 업로드 성공
                    _uploadState.value = ImageUploadState.Success
                    _selectedImageUris.value = emptyList() // 성공 후 목록 비우기
                } catch (e: Exception) {
                    // 업로드 과정 중 하나라도 실패하면
                    Log.e("ImageUploadViewModel", "업로드 실패", e)
                    _uploadState.value = ImageUploadState.Error(e.message ?: "업로드에 실패했습니다.")
                }
            }
        }*/
}