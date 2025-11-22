package com.hongildong.map.data.module

import com.hongildong.map.data.remote.api.AuthService
import com.hongildong.map.data.remote.api.BookmarkService
import com.hongildong.map.data.remote.api.MemberService
import com.hongildong.map.data.remote.api.ReviewService
import com.hongildong.map.data.remote.api.SearchService
import com.hongildong.map.data.repository.AuthRepository
import com.hongildong.map.data.repository.AuthRepositoryImpl
import com.hongildong.map.data.repository.BookmarkRepository
import com.hongildong.map.data.repository.BookmarkRepositoryImpl
import com.hongildong.map.data.repository.MemberRepository
import com.hongildong.map.data.repository.MemberRepositoryImpl
import com.hongildong.map.data.repository.ReviewRepository
import com.hongildong.map.data.repository.ReviewRepositoryImpl
import com.hongildong.map.data.repository.SearchRepository
import com.hongildong.map.data.repository.SearchRepositoryImpl
import com.hongildong.map.data.util.ImageRepository
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

    @Provides
    @Singleton
    fun provideBookmarkRepository(
        bookmarkService: BookmarkService
    ) : BookmarkRepository = BookmarkRepositoryImpl(api = bookmarkService)

    @Provides
    @Singleton
    fun provideReviewRepository(
        reviewService: ReviewService
    ) : ReviewRepository = ReviewRepositoryImpl(api = reviewService)

    @Provides
    @Singleton
    fun provideMemberRepository(
        memberService: MemberService
    ) : MemberRepository = MemberRepositoryImpl(api = memberService)

}