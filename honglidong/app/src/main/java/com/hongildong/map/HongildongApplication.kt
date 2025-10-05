package com.hongildong.map

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
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