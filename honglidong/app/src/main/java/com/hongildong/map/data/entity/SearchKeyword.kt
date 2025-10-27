package com.hongildong.map.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_keywords")
data class SearchKeyword (
    @PrimaryKey
    val keyword: String,
    val nodeId: Int,
    val nodeCode: String,
    val timestamp: Long = System.currentTimeMillis()
)