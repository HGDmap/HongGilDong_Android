package com.hongildong.map.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "search_keywords",
    primaryKeys = ["id", "node_code"]
)
data class SearchKeyword (
    @ColumnInfo(name = "node_name") val nodeName: String,
    @ColumnInfo(name = "id") val id: Int, // 시설, 이벤트, 건물 id
    @ColumnInfo(name = "node_id") val nodeId: Int, // 연결된 노드의 id (길찾기용)
    @ColumnInfo(name = "node_code") val nodeCode: String,
    @ColumnInfo(name = "timestamp") val timestamp: Long = System.currentTimeMillis()
)