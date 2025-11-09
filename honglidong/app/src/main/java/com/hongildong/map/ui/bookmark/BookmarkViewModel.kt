package com.hongildong.map.ui.bookmark

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hongildong.map.data.remote.request.BookmarkFolderUpdateRequest
import com.hongildong.map.data.entity.BookmarkFolder
import com.hongildong.map.data.repository.BookmarkRepository
import com.hongildong.map.data.util.DefaultResponse
import com.hongildong.map.ui.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
    @ApplicationContext private val context: Context
): ViewModel() {
    private val TAG = this.javaClass.simpleName

    // 토큰
    private val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    fun getToken(): String? {
        return sharedPreferences.getString("access_token", null)
    }

    private val _isUser = MutableStateFlow<Boolean>(false)
    val isUser: StateFlow<Boolean> = _isUser.asStateFlow()

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

    // 로딩
    private val _loading = MutableStateFlow<UiState>(UiState.Initial)
    val loading: StateFlow<UiState> = _loading.asStateFlow()

    // 모든 북마크 리스트
    private val _allBookmarkInfo = MutableStateFlow<List<BookmarkFolder>>(listOf())
    val allBookmarkInfo: StateFlow<List<BookmarkFolder>> = _allBookmarkInfo.asStateFlow()

    // 모든 북마크 리스트 가져오기
    fun getAllBookmarks() {
        viewModelScope.launch {
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                return@launch
            }

            _loading.value = UiState.Loading
            val response = bookmarkRepository.getAllBookmarks(token)
            when (response) {
                is DefaultResponse.Success -> {
                    Log.d(TAG, "응답 성공: $response")
                    _allBookmarkInfo.value = response.data.bookmarkFolderList
                    Log.d(TAG, "allBookmarkInfo: ${allBookmarkInfo.value}")
                    _loading.value = UiState.Success
                }
                is DefaultResponse.Error -> {
                    Log.d(TAG, "응답 실패: $response")
                    _loading.value = UiState.Error(response.message)
                }
            }
        }
    }

    // 수정, 추가된 북마크 폴더 정보
    private val _folderInfo = MutableStateFlow<BookmarkFolder?>(null)
    val folderInfo: StateFlow<BookmarkFolder?> = _folderInfo.asStateFlow()

    // 북마크 추가 수정
    fun updateBookmark(
        type: String,
        folderId: Int,
        targetId: Int
    ) {
        viewModelScope.launch {
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                return@launch
            }

            _loading.value = UiState.Loading
            val response = bookmarkRepository.update(token, type, folderId, targetId)
            when (response) {
                is DefaultResponse.Success -> {
                    Log.d(TAG, "응답 성공: $response")
                    _folderInfo.value = response.data
                    Log.d(TAG, "updated folderInfo: ${folderInfo.value}")
                    _loading.value = UiState.Success
                }
                is DefaultResponse.Error -> {
                    Log.d(TAG, "응답 실패: $response")
                    _loading.value = UiState.Error(response.message)
                }
            }
        }
    }

    // 북마크 추가 수정
    fun deleteBookmark(
        type: String,
        targetId: Int
    ) {
        viewModelScope.launch {
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                return@launch
            }

            _loading.value = UiState.Loading
            val response = bookmarkRepository.delete(token, type, targetId)
            when (response) {
                is DefaultResponse.Success -> {
                    Log.d(TAG, "응답 성공: $response")
                    _folderInfo.value = response.data
                    Log.d(TAG, "updated folderInfo: ${folderInfo.value}")
                    _loading.value = UiState.Success
                }
                is DefaultResponse.Error -> {
                    Log.d(TAG, "응답 실패: $response")
                    _loading.value = UiState.Error(response.message)
                }
            }
        }
    }

    // 북마크 폴더

    // 북마크 폴더 추가
    fun addFolder(
        folderName: String,
        folderColor: String
    ) {
        viewModelScope.launch {
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                return@launch
            }

            _loading.value = UiState.Loading
            val body = BookmarkFolderUpdateRequest(
                folderName = folderName,
                folderColor = folderColor
            )

            val response = bookmarkRepository.addFolder(token, body)
            when (response) {
                is DefaultResponse.Success -> {
                    Log.d(TAG, "응답 성공: $response")
                    _allBookmarkInfo.value = response.data.bookmarkFolderList
                    Log.d(TAG, "updated bookmark info: ${allBookmarkInfo.value}")
                    _loading.value = UiState.Success
                }
                is DefaultResponse.Error -> {
                    Log.d(TAG, "응답 실패: $response")
                    _loading.value = UiState.Error(response.message)
                }
            }
        }
    }

    fun updateFolder(
        folderId: Int,
        folderName: String,
        folderColor: String
    ) {
        viewModelScope.launch {
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                return@launch
            }

            _loading.value = UiState.Loading
            val body = BookmarkFolderUpdateRequest(
                folderName = folderName,
                folderColor = folderColor
            )
            val response = bookmarkRepository.updateFolder(token, folderId, body)
            when (response) {
                is DefaultResponse.Success -> {
                    Log.d(TAG, "응답 성공: $response")
                    _allBookmarkInfo.value = response.data.bookmarkFolderList
                    Log.d(TAG, "updated bookmark info: ${allBookmarkInfo.value}")
                    _loading.value = UiState.Success
                }
                is DefaultResponse.Error -> {
                    Log.d(TAG, "응답 실패: $response")
                    _loading.value = UiState.Error(response.message)
                }
            }
        }
    }

    fun deleteFolder(
        folderId: Int
    ) {
        viewModelScope.launch {
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                return@launch
            }

            _loading.value = UiState.Loading
            val response = bookmarkRepository.deleteFolder(token, folderId)
            when (response) {
                is DefaultResponse.Success -> {
                    Log.d(TAG, "응답 성공: $response")
                    _allBookmarkInfo.value = response.data.bookmarkFolderList
                    Log.d(TAG, "updated bookmark info: ${allBookmarkInfo.value}")
                    _loading.value = UiState.Success
                }
                is DefaultResponse.Error -> {
                    Log.d(TAG, "응답 실패: $response")
                    _loading.value = UiState.Error(response.message)
                }
            }
        }
    }

    fun getBookmarksOfFolder(
        folderId: Int
    ) {
        viewModelScope.launch {
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                return@launch
            }

            _loading.value = UiState.Loading
            val response = bookmarkRepository.getBookmarksOfFolder(token, folderId)
            when (response) {
                is DefaultResponse.Success -> {
                    Log.d(TAG, "응답 성공: $response")
                    _folderInfo.value = response.data
                    Log.d(TAG, "updated folderInfo: ${folderInfo.value}")
                    _loading.value = UiState.Success
                }
                is DefaultResponse.Error -> {
                    Log.d(TAG, "응답 실패: $response")
                    _loading.value = UiState.Error(response.message)
                }
            }
        }
    }
}