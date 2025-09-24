package com.hongildong.map.ui.user.signup

import androidx.lifecycle.ViewModel
import com.hongildong.map.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class SignupViewmodel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {
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
}