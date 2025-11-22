package com.hongildong.map.ui.profile

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hongildong.map.data.entity.ReviewInfo
import com.hongildong.map.data.remote.request.ImageUploadRequest
import com.hongildong.map.data.remote.request.ProfileUpdateRequest
import com.hongildong.map.data.remote.response.ImageUploadResponse
import com.hongildong.map.data.remote.response.ProfileResponse
import com.hongildong.map.data.remote.response.ReviewResponse
import com.hongildong.map.data.repository.MemberRepository
import com.hongildong.map.data.util.DefaultResponse
import com.hongildong.map.data.util.ImageRepository
import com.hongildong.map.ui.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MemberViewmodel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val memberRepository: MemberRepository,
    private val imageRepository: ImageRepository,
): ViewModel() {
    private val TAG = this.javaClass.simpleName

    // 토큰
    private val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    fun getToken(): String? {
        return sharedPreferences.getString("access_token", null)
    }

    private val _isUser = MutableStateFlow(false)
    val isUser = _isUser.asStateFlow()

    fun verifyUser() {
        viewModelScope.launch {
            val token = getToken()
            Log.d("token check", "$token")
            if (token != null) {
                _isUser.value = true
                return@launch
            } else {
                _isUser.value = false
                return@launch
            }
        }
    }

    private val _uploadState = MutableStateFlow<UiState>(UiState.Initial)
    val uploadState = _uploadState.asStateFlow()

    // 로컬 uri과 presigned url / image url 합친 리스트
    private val _serverUrls = MutableStateFlow<List<Pair<Uri, ImageUploadResponse>>>(emptyList())

    // 회원 프로필 수정
    fun updateProfile(
        userId: Int,
        nickname: String,
        pickedImage: String?,
    ) {
        viewModelScope.launch {
            try {
                val token = getToken() ?: throw Exception("토큰이 없습니다.")

                _uploadState.value = UiState.Loading
                val uploadedImageUrls: List<String?>
                if (pickedImage.isNullOrEmpty()) {
                    // 프로필 이미지 삭제시 프로필 업데이트
                    uploadedImageUrls = listOf(null)
                } else {
                    // 이미지가 있는 프로필 업데이트

                    // uri 리스트로 presigned url 받아오기
                    val fileName = imageRepository.getFileName(pickedImage.toUri()) ?: ""

                    val imageUploadRequest = ImageUploadRequest(
                        id = userId,
                        fileNames = listOf(fileName)
                    )

                    Log.d(TAG, "create presigned url 요청: $imageUploadRequest")

                    val presignedUrlResponse =
                        memberRepository.createPresignedUrl(token, imageUploadRequest)

                    when (presignedUrlResponse) {
                        is DefaultResponse.Success -> {
                            Log.d(TAG, "create presigned url 응답 성공: $presignedUrlResponse")
                            _serverUrls.value =
                                listOf(pickedImage.toUri()).zip(presignedUrlResponse.data)
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
                val body = ProfileUpdateRequest(
                    nickname = nickname,
                    profilePic = uploadedImageUrls[0]
                )

                // 리뷰 업데이트
                val response = memberRepository.updateProfile(token, body)
                when (response) {
                    is DefaultResponse.Success -> {
                        Log.d(TAG, "프로필 업데이트 성공")
                        _serverUrls.value = emptyList() // 성공 후 url 정보 비우기
                        _uploadState.value = UiState.Success
                        _profileInfo.value = response.data
                    }
                    is DefaultResponse.Error -> {
                        throw Exception(response.message)
                    }
                }

            } catch (e: Exception) {
                Log.e(TAG, "프로필 업데이트 실패", e)
                _uploadState.value = UiState.Error(e.message ?: "프로필 업데이트에 실패했습니다.")
            }
        }
    }

    private val _profileInfo = MutableStateFlow<ProfileResponse?>(null)
    val profileInfo = _profileInfo.asStateFlow()

    // 회원 정보 조회
    fun getProfile() {
        viewModelScope.launch {
            val token = getToken() ?: return@launch

            val response = memberRepository.getProfile(token)
            when (response) {
                is DefaultResponse.Success -> {
                    Log.d(TAG, "프로필 조회 성공: $response")
                    _profileInfo.value = response.data
                }
                is DefaultResponse.Error -> {
                    Log.d(TAG, "프로필 조회 실패: $response")
                }
            }
        }
    }

    private val _myReviews = MutableStateFlow<List<ReviewInfo>>(emptyList())
    val myReviews = _myReviews.asStateFlow()

    // 내가 쓴 리뷰 조회
    fun getMyReviews() {
        viewModelScope.launch {
            val token = getToken() ?: return@launch

            val page = 0
            val size = 30

            val response = memberRepository.getMyReviews(token, page, size)
            when (response) {
                is DefaultResponse.Success -> {
                    Log.d(TAG, "내가 쓴 리뷰 조회 성공: $response")
                    _myReviews.value = response.data.reviewList
                }
                is DefaultResponse.Error -> {
                    Log.d(TAG, "내가 쓴 리뷰 조회 실패: $response")
                }
            }
        }
    }

    private val _likedReviews = MutableStateFlow<List<ReviewInfo>>(emptyList())
    val likedReviews = _likedReviews.asStateFlow()

    // 좋아요한 리뷰 조회
    fun getLikedReview() {
        viewModelScope.launch {
            val token = getToken() ?: return@launch

            val page = 0
            val size = 30

            val response = memberRepository.getLikeReviews(token, page, size)
            when (response) {
                is DefaultResponse.Success -> {
                    Log.d(TAG, "좋아요한 리뷰 조회 성공: $response")
                    _likedReviews.value = response.data.reviewList
                }
                is DefaultResponse.Error -> {
                    Log.d(TAG, "좋아요한 리뷰 조회 실패: $response")
                }
            }
        }
    }
}