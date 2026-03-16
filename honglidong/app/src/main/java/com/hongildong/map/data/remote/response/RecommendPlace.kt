package com.hongildong.map.data.remote.response

import com.hongildong.map.data.entity.RecommendFacilityInfo

data class RecommendPlace(
    val facilityList: List<RecommendFacilityInfo>,
    val hashTag: String
)