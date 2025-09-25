package com.hongildong.map.ui.user.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hongildong.map.data.remote.request.SignupRequest
import com.hongildong.map.data.repository.AuthRepository
import com.hongildong.map.data.util.DefaultResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SignupViewmodel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {
    private val TAG = this.javaClass.simpleName

}