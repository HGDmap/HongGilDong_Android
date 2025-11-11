package com.hongildong.map.data.module

import com.hongildong.map.data.remote.api.AuthService
import com.hongildong.map.data.remote.api.BookmarkService
import com.hongildong.map.data.remote.api.SearchService
import com.hongildong.map.data.util.safeApiCall
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    private inline fun <reified T> Retrofit.buildService(): T {
        return this.create(T::class.java)
    }

    @Provides
    @Singleton
    fun authApi(@NetworkModule.BaseRetrofit retrofit: Retrofit): AuthService {
        return retrofit.buildService()
    }

    @Provides
    @Singleton
    fun searchApi(@NetworkModule.BaseRetrofit retrofit: Retrofit): SearchService {
        return retrofit.buildService()
    }

    @Provides
    @Singleton
    fun bookmarkApi(@NetworkModule.BaseRetrofit retrofit: Retrofit): BookmarkService {
        return retrofit.buildService()
    }
}