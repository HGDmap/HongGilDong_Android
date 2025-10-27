package com.hongildong.map.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hongildong.map.data.entity.SearchKeyword
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchKeywordDao {
    // 최근 검색어를 시간 순으로 가져오기 (최대 10개)
    @Query("SELECT * FROM search_keywords ORDER BY timestamp DESC LIMIT 10")
    fun getRecentKeywords(): Flow<List<SearchKeyword>>

    // 검색어 추가 또는 업데이트 (이미 있는 검색어면 시간만 갱신)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKeyword(keyword: SearchKeyword)

    // 특정 검색어 삭제
    @Query("DELETE FROM search_keywords WHERE node_id = :nodeId and node_code = :nodeCode")
    suspend fun deleteKeyword(nodeId: Int, nodeCode: String)

    // 전체 검색어 삭제
    @Query("DELETE FROM search_keywords")
    suspend fun clearAllKeywords()
}