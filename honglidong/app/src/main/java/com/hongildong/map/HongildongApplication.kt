package com.hongildong.map

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.hongildong.map.ui.user.signup.AuthViewmodel
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HongildongApplication : Application(), DefaultLifecycleObserver {

    override fun onCreate() {
        super<Application>.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // 앱 상태 감지 시작
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }
}