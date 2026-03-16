package com.hongildong.map.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hongildong.map.R
import com.hongildong.map.data.entity.Floor
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Black
import com.hongildong.map.ui.theme.White

enum class FacilityType(
    val apiName: String,
    val displayName: String,
    val icon: Int,
) {
    EVENT("EVENT", "이벤트", R.drawable.img_event_color),
    CAFE("CAFE","카페", R.drawable.img_cafe_color),
    LOUNGE("LOUNGE", "휴게실", R.drawable.img_lounge_color),
    VENDING_MACHINE("MACHINE","무인 기계", R.drawable.img_vendingmachine_color),
    TOILET("TOILET","화장실", R.drawable.img_toilet_color),
    SMOKING_AREA("SMOKING_AREA","흡연구역", R.drawable.img_smoking_color),
    STUDY_ROOM("STUDY","열람실", R.drawable.img_study_color);

    companion object {
        // apiName 기반으로 enum 상수 검색
        fun fromApiName(apiName: String): FacilityType? {
            return FacilityType.entries.find { it.apiName.equals(apiName, ignoreCase = true) }
        }
        // displayName 기반으로 enum 상수 검색
        fun fromDisplayName(displayName: String): FacilityType? {
            return FacilityType.entries.find { it.displayName.equals(displayName, ignoreCase = true) }
        }
    }
}

@Composable
fun FacilityTypeTags(
    onClick: (String) -> Unit = {}
) {
    val tags = FacilityType.entries
    LazyRow (
        modifier = Modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(start = 15.dp, end = 15.dp)
    ) {
        items(tags) { tag ->
            FacilityTypeTagItem(tag.icon, tag.displayName, { onClick(tag.apiName) })
        }
    }
}

@Composable
fun FacilityTypeTagItem(
    tagIcon: Int,
    tagTitle: String,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .padding(5.dp)
            .shadow(6.dp, shape = RoundedCornerShape(20.dp))
            .wrapContentSize()
            .clickable {onClick()}
            .background(color = White, shape = RoundedCornerShape(size = 20.dp))
            .padding(vertical = 5.dp, horizontal = 10.dp)
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(tagIcon),
                contentDescription = tagTitle,
                modifier = Modifier.size(15.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                tagTitle,
                style = AppTypography.Regular_13.copy(color = Black)
            )
        }
    }
}
