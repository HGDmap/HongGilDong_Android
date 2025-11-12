package com.hongildong.map.data.remote.request

data class PhotoRequest(
    val continuationToken: String, // 사진 리스트의 다음 페이지를 받아오기 위한 토큰 (서버에서 보내줄것임)
    val size: Int // 사진 리스트의 페이지 크기 (한번에 받아올 사진 개수)
)