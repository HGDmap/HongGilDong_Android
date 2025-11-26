package com.hongildong.map.data.remote.response

import com.hongildong.map.data.entity.FacilityInfo

data class RecommendPlaceResponse(
    val facilityList: List<FacilityInfo>,
    val hashTag: String
)