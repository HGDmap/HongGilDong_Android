package com.hongildong.map.data.module

import com.hongildong.map.data.remote.api.AuthService
import com.hongildong.map.data.remote.api.SearchService
import com.hongildong.map.data.repository.AuthRepository
import com.hongildong.map.data.repository.AuthRepositoryImpl
import com.hongildong.map.data.repository.SearchRepository
import com.hongildong.map.data.repository.SearchRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        authService: AuthService
    ) : AuthRepository = AuthRepositoryImpl(api = authService)

    @Provides
    @Singleton
    fun provideSearchRepository(
        searchService: SearchService
    ) : SearchRepository = SearchRepositoryImpl(api = searchService)
}