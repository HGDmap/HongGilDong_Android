package com.hongildong.map.data.remote.request

data class ImageUploadRequest(
    val fileNames: List<String>, // member profile 업데이트일 경우 하나만. 업데이트하지 않을 경우에도 기존 url 보내야함
    val id: Int // member profile 업데이트일 경우 member id
)