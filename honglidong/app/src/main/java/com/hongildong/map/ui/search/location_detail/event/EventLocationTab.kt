package com.hongildong.map.ui.search.location_detail.event

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hongildong.map.R
import com.hongildong.map.data.entity.EventDetailInfo
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Black
import com.hongildong.map.ui.theme.Gray600
import com.hongildong.map.ui.util.ButtonWithIcon
import com.hongildong.map.ui.util.NetworkImage

@Composable
fun EventLocationTab(
    eventInfo: EventDetailInfo,
    onClick: (EventDetailInfo) -> Unit,
    onDirect: (EventDetailInfo) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onClick(eventInfo)
                },
        ) {
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        eventInfo.locationInfo.buildingName ?: "temp",
                        style = AppTypography.Medium_18.copy(color = Black)
                    )
                    Spacer(Modifier.height(3.dp))
                    Text(
                        eventInfo.location ?: "",
                        style = AppTypography.Medium_13.copy(color = Gray600)
                    )
                }
                ButtonWithIcon(
                    icon = R.drawable.ic_direction,
                    title = "길찾기",
                    onClick = { onDirect(eventInfo) }
                )
            }
            Spacer(Modifier.height(12.dp))
            LazyRow (
                modifier = Modifier
                    .height(120.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(eventInfo.locationInfo.images) { image ->
                    // 네트워크 이미지 로더 추가 필요
                    NetworkImage(
                        url = image,
                        height = 150.dp,
                        contentDescription = "",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}