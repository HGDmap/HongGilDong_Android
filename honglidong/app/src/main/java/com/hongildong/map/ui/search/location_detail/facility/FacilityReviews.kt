package com.hongildong.map.ui.search.location_detail.facility

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Black
import com.hongildong.map.ui.theme.Gray300
import com.hongildong.map.ui.theme.Gray500
import com.hongildong.map.ui.util.NetworkImage
import com.hongildong.map.ui.util.ProfileImage
import com.hongildong.map.ui.util.popup.DropDownMenu

@Composable
fun FacilityReviews(
    isUser: Boolean
) {
    Column {
        Text(
            "리뷰",
            style = AppTypography.Bold_20.copy(color = Black),
            modifier = Modifier.padding(vertical = 25.dp)
        )
        LazyColumn {
            items(8) {
                FacilityReviewItem()
            }
        }
    }
}

@Preview
@Composable
fun FacilityReviewItem() {
    Column {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                ProfileImage()
                Spacer(Modifier.width(4.dp))
                Text(
                    "홍길동",
                    style = AppTypography.Bold_18.copy(color = Black)
                )
            }

            DropDownMenu(
                onDelete = {},
                onEdit = {}
            )
        }
        Row (
            modifier = Modifier.padding(vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RateImage(
                width = 101.dp,
                height = 19.dp,
                rate = 0.8f
            )
            Spacer(Modifier.width(4.dp))
            Text(
                "2025.04.23",
                style = AppTypography.Medium_13.copy(color = Gray500)
            )
        }

        Text(
            "뚜루뚜루뚜뚜뚜뚜루뚜루뚜뚜뚜뚜루뚜루뚜뚜뚜뚜루뚜루뚜뚜뚜뚜루뚜루뚜뚜뚜뚜루뚜루뚜뚜뚜뚜루뚜루뚜뚜뚜뚜루뚜루뚜뚜뚜뚜루뚜루뚜뚜뚜뚜루뚜루뚜뚜뚜뚜루뚜루뚜뚜뚜뚜루뚜루뚜뚜뚜뚜루뚜루뚜뚜뚜뚜루뚜루뚜뚜뚜뚜루뚜루뚜뚜뚜뚜루뚜루뚜뚜뚜뚜루뚜루뚜뚜뚜",
            style = AppTypography.Medium_15.copy(color = Black),
            modifier = Modifier
                .fillMaxWidth()
        )
        LazyRow {
            items(3) {
                NetworkImage(
                    url = "https://www.fitpetmall.com/wp-content/uploads/2023/10/shutterstock_1844153299-1024x683-1.png",
                    contentDescription = null,
                    width = 180.dp,
                    height = 140.dp,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
        // 좋아요 아이콘
        Spacer(Modifier.height(8.dp))
        HorizontalDivider(thickness = 1.dp, color = Gray300)
    }
}