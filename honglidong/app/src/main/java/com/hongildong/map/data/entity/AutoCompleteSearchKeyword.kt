package com.hongildong.map.data.entity

data class AutoCompleteSearchKeyword(
    val id: Int,
    val name: String,
    val type: String,
    val nodeId: Int,
)

// 자동완성 클래스를 검색 클래스로 변환하는 함수
fun AutoCompleteSearchKeyword.toSearchKeyword(): SearchKeyword {
    return SearchKeyword(
        nodeName = this.name,
        id = this.id,
        nodeId = this.nodeId,
        nodeCode = this.type,
    )
}