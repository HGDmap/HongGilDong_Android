package com.hongildong.map.ui.user.signup

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hongildong.map.data.remote.request.SigninRequest
import com.hongildong.map.data.remote.request.SignupRequest
import com.hongildong.map.data.repository.AuthRepository
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
class AuthViewmodel @Inject constructor(
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context
): ViewModel() {

    private val TAG = this.javaClass.simpleName

    // 이메일 상태
    private val _emailInfo = MutableStateFlow<String>("")
    val emailInfo: StateFlow<String> = _emailInfo.asStateFlow()

    // 비밀번호 상태
    private val _passwordInfo = MutableStateFlow<String>("")
    val passwordInfo: StateFlow<String> = _passwordInfo.asStateFlow()

    // 실명 상태
    private val _nameInfo = MutableStateFlow<String>("")
    val nameInfo: StateFlow<String> = _nameInfo.asStateFlow()

    // 닉네임 상태
    private val _nicknameInfo = MutableStateFlow<String>("")
    val nicknameInfo: StateFlow<String> = _nicknameInfo.asStateFlow()

    // 전공강의동 상태
    private val _buildingInfo = MutableStateFlow<String?>(null)
    val buildingInfo: StateFlow<String?> = _buildingInfo.asStateFlow()

    private val _isSignInSuccess = MutableStateFlow<UiState>(UiState.Initial)
    val isSignInSuccess: StateFlow<UiState> = _isSignInSuccess.asStateFlow()

    private val _isSignUpSuccess = MutableStateFlow<Boolean>(false)
    val isSignUpSuccess: StateFlow<Boolean> = _isSignUpSuccess.asStateFlow()
    private val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    fun getToken(): String? {
        return sharedPreferences.getString("access_token", null)
    }



    // 이메일 상태 변경
    fun onEmailInfoChange(newState: String) {
        _emailInfo.value = newState
    }

    // 비밀번호 상태 변경
    fun onPasswordInfoChange(newState: String) {
        _passwordInfo.value = newState
    }

    // 실명 상태 변경
    fun onNameInfoChange(newState: String) {
        _nameInfo.value = newState
    }

    // 닉네임 상태 변경
    fun onNicknameInfoChange(newState: String) {
        _nicknameInfo.value = newState
    }

    // 전공강의동 상태 변경
    fun onBuildingInfoChange(newState: String?) {
        _buildingInfo.value = newState
    }

    fun signin(body: SigninRequest) {
        viewModelScope.launch {
            val response = authRepository.signin(body)
            when (response) {
                is DefaultResponse.Success -> {
                    Log.d(TAG, "응답 성공: $response")
                    if (response.data == null) {
                        Log.d(TAG, "유효하지 않은 토큰입니다.")
                        _isSignInSuccess.value = UiState.Error("유효하지 않은 토큰입니다.")
                        return@launch
                    }
                    sharedPreferences.edit {
                        putString("access_token", "Bearer " + response.data.accessToken)
                        putString("refresh_token", "Bearer " + response.data.refreshToken)
                    }
                    _isSignInSuccess.value = UiState.Success
                }
                is DefaultResponse.Error -> {
                    Log.d(TAG, "응답 실패: $response")
                    _isSignInSuccess.value = UiState.Error("유효하지 않은 회원입니다.")
                }
            }
        }
    }

    fun signup() {
        viewModelScope.launch {
            val body = SignupRequest(
                email = _emailInfo.value,
                fullName = _nameInfo.value,
                nickname = _nicknameInfo.value,
                password = _passwordInfo.value
            )

            val response = authRepository.signup(body)
            when (response) {
                is DefaultResponse.Success -> {
                    Log.d(TAG, "응답 성공: $response")
                    _isSignUpSuccess.value = true
                }
                is DefaultResponse.Error -> {
                    Log.d(TAG, "응답 실패: $response")
                    _isSignUpSuccess.value = false
                }
            }
        }
    }
}