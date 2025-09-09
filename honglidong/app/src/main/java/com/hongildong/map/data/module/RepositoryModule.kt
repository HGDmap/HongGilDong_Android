package com.hongildong.map.data.module

import com.hongildong.map.data.repository.SignupRepository
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
    fun provideSignupRepository(): SignupRepository {
        return SignupRepository()
    }
}