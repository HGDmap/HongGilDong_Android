package com.hongildong.map.ui.search.location_detail.facility

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hongildong.map.R
import com.hongildong.map.data.entity.FacilityInfo
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Black
import com.hongildong.map.ui.theme.PrimaryLight

// 시설 정보 - 시설 정보 탭
@Composable
fun FacilityInfoTab(
    facilityInfo: FacilityInfo
) {
    Column(
        modifier = Modifier.fillMaxSize(1f)
    ) {
        IconWithText(icon = R.drawable.ic_location_info, text = facilityInfo.description)
        IconWithText(icon = R.drawable.ic_location_open, text = facilityInfo.open ?: "영업중")
        if (!facilityInfo.phone.isNullOrEmpty()) {
            IconWithText(icon = R.drawable.ic_location_phone, text = facilityInfo.phone)
        }
        if (!facilityInfo.link.isNullOrEmpty()) {
            IconWithText(icon = R.drawable.ic_location_link, text = facilityInfo.link)
        }
    }
}


@Composable
fun IconWithText(
    icon: Int,
    text: String
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = "",
            modifier = Modifier.padding(end = 5.dp),
            tint = PrimaryLight
        )
        Text(
            text = text,
            style = AppTypography.Medium_15.copy(color = Black),
            modifier = Modifier.weight(1f)
        )
    }
}