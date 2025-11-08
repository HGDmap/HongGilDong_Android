package com.hongildong.map.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hongildong.map.data.dao.SearchKeywordDao
import com.hongildong.map.data.entity.SearchKeyword

@Database(
    entities = [SearchKeyword::class],
    version = 3,
)
abstract class SearchDatabase : RoomDatabase() {
    abstract fun searchKeywordDao(): SearchKeywordDao
}