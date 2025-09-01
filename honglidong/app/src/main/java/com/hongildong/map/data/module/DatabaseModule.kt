package com.hongildong.map.data.module

import android.content.Context
import androidx.room.Room
import com.hongildong.map.data.dao.SearchKeywordDao
import com.hongildong.map.data.db.SearchDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideSearchDatabase(@ApplicationContext context: Context): SearchDatabase {
        return Room.databaseBuilder(
            context,
            SearchDatabase::class.java,
            "search_keyword_db"
        ).build()
    }

    @Provides
    fun provideSearchKeywordDao(searchDatabase: SearchDatabase): SearchKeywordDao {
        return searchDatabase.searchKeywordDao()
    }
}