package com.hongildong.map.ui.user.signup

import android.content.Context
import androidx.lifecycle.ViewModel
import com.hongildong.map.data.remote.request.SigninRequest
import com.hongildong.map.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class AuthViewmodel @Inject constructor(
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context
): ViewModel() {

    private val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    fun getToken(): String? {
        return sharedPreferences.getString("access_token", null)
    }

    fun signin(body: SigninRequest) {

    }
}