package com.hongildong.map.ui.search.location_detail.facility.review

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hongildong.map.data.entity.FacilityInfo
import com.hongildong.map.data.entity.ReviewInfo
import com.hongildong.map.data.remote.request.ImageUploadRequest
import com.hongildong.map.data.remote.request.ReviewUpdateRequest
import com.hongildong.map.data.remote.response.ImageUploadResponse
import com.hongildong.map.data.repository.ReviewRepository
import com.hongildong.map.data.util.DefaultResponse
import com.hongildong.map.data.util.ImageRepository
import com.hongildong.map.ui.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val imageRepository: ImageRepository,
    @ApplicationContext private val context: Context
): ViewModel() {

    fun clearReviewInfo() {
        viewModelScope.launch {
            _selectedImageUris.value = emptyList()
            _serverUrls.value = emptyList()
            _uploadState.value = UiState.Initial
        }
    }

    private val _targetReview = MutableStateFlow<ReviewInfo?>(null)
    val targetReview = _targetReview.asStateFlow()

    fun setTargetReview(review: ReviewInfo) {
        viewModelScope.launch {
            _targetReview.value = review
        }
    }

    private val _targetFacility = MutableStateFlow<FacilityInfo?>(null)
    val targetFacility = _targetFacility.asStateFlow()

    fun setTargetFacility(facility: FacilityInfo) {
        viewModelScope.launch {
            _targetFacility.value = facility
        }
    }

    // 로컬 이미지 uri 리스트
    private val _selectedImageUris = MutableStateFlow<List<Uri>>(emptyList())
    val selectedImageUris = _selectedImageUris.asStateFlow()

    // 로컬 uri과 presigned url / image url 합친 리스트
    private val _serverUrls = MutableStateFlow<List<Pair<Uri, ImageUploadResponse>>>(emptyList())

    private val _uploadState = MutableStateFlow<UiState>(UiState.Initial)
    val uploadState = _uploadState.asStateFlow()

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

    // 리뷰 새로 등록 or 업데이트
    fun updateReview(
        isNewReview: Boolean = true,
        facilityId: Int = 0,
        content: String,
    ) {
        viewModelScope.launch {
            try {
                val token = getToken() ?: throw Exception("토큰이 없습니다.")

                _uploadState.value = UiState.Loading
                val uploadedImageUrls: List<String>
                if (_selectedImageUris.value.isEmpty()) {
                    // 이미지가 없는 리뷰 등록
                    uploadedImageUrls = emptyList()
                } else {
                    // 이미지 있는 리뷰 등록
                    // uri 리스트로 presigned url 받아오기
                    val fileNames = _selectedImageUris.value.map { uri ->
                        imageRepository.getFileName(uri) ?: ""
                    }

                    val imageUploadRequest = ImageUploadRequest(
                        facilityId = facilityId,
                        fileNames = fileNames
                    )
                    Log.d(TAG, "create presigned url 요청: $imageUploadRequest")

                    val presignedUrlResponse =
                        reviewRepository.createPresignedUrl(token, imageUploadRequest)
                    when (presignedUrlResponse) {
                        is DefaultResponse.Success -> {
                            Log.d(TAG, "create presigned url 응답 성공: $presignedUrlResponse")
                            _serverUrls.value =
                                _selectedImageUris.value.zip(presignedUrlResponse.data)
                        }

                        is DefaultResponse.Error -> {
                            Log.d(TAG, "create presigned url 응답 실패: $presignedUrlResponse")
                            _uploadState.value = UiState.Error(presignedUrlResponse.message)
                            throw Exception("presigned url 요청 실패: ${presignedUrlResponse.message}")
                        }
                    }

                    // s3에 이미지 업로드
                    coroutineScope {
                        val uploadJobs = _serverUrls.value.map { (uri, response) ->
                            async {
                                val result =
                                    imageRepository.uploadImageToS3(response.presignedURL, uri)
                                if (result.isFailure) {
                                    Log.e(TAG, "s3 업로드 실패: ${result.exceptionOrNull()?.message}")
                                    throw Exception("s3 이미지 업로드 실패: ${result.exceptionOrNull()?.message}")
                                } else {
                                    Log.d(TAG, "s3 이미지 업로드 성공")
                                }
                            }
                        }
                        uploadJobs.awaitAll() // 병렬 처리가 모두 완료될때까지 기다리기
                    }

                    uploadedImageUrls = _serverUrls.value.map { it.second.imageURL }
                }

                // imageUrl로 api 요청
                val body = ReviewUpdateRequest(
                    content = content,
                    photoList = uploadedImageUrls
                )

                if (isNewReview) {
                    // 리뷰 새로 만들기
                    val response = reviewRepository.createReview(token, facilityId, body)
                    when (response) {
                        is DefaultResponse.Success -> {
                            Log.d("ReviewViewModel", "리뷰 등록 성공")
                            _selectedImageUris.value = emptyList() // 성공 후 이미지 비우기
                            _serverUrls.value = emptyList() // 성공 후 url 정보 비우기
                            _uploadState.value = UiState.Success
                        }
                        is DefaultResponse.Error -> {
                            throw Exception(response.message)
                        }
                    }
                } else {
                    val reviewId = _targetReview.value?.id ?: throw Exception("리뷰 id가 없습니다.")
                    // 리뷰 업데이트
                    val response = reviewRepository.updateReview(token, reviewId, body)
                    when (response) {
                        is DefaultResponse.Success -> {
                            Log.d("ReviewViewModel", "리뷰 업데이트 성공")
                            _selectedImageUris.value = emptyList() // 성공 후 이미지 비우기
                            _serverUrls.value = emptyList() // 성공 후 url 정보 비우기
                            _uploadState.value = UiState.Success
                        }
                        is DefaultResponse.Error -> {
                            throw Exception(response.message)
                        }
                    }
                }

            } catch (e: Exception) {
                Log.e(TAG, "리뷰 등록 실패", e)
                _uploadState.value = UiState.Error(e.message ?: "리뷰 등록에 실패했습니다.")
            }
        }
    }

    fun deleteReview(
        reviewId: Int
    ) {
        viewModelScope.launch {
            val token = getToken() ?: return@launch

            val response = reviewRepository.deleteReview(token, reviewId)
            when (response) {
                is DefaultResponse.Success -> {
                    Log.d(TAG, "리뷰 삭제 성공")
                    _selectedImageUris.value = emptyList() // 성공 후 이미지 비우기
                    _serverUrls.value = emptyList() // 성공 후 url 정보 비우기
                    _uploadState.value = UiState.Success
                }
                is DefaultResponse.Error -> {
                    Log.d(TAG, "리뷰 삭제 실패")
                }
            }
        }
    }
}