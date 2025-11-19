package com.hongildong.map.ui.search.location_detail.facility

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hongildong.map.R
import com.hongildong.map.ui.search.SearchKeywordViewmodel
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Black
import com.hongildong.map.ui.theme.Gray100
import com.hongildong.map.ui.theme.Gray300
import com.hongildong.map.ui.theme.PrimaryMid
import com.hongildong.map.ui.theme.White
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.materials.HazeMaterials

@Composable
fun FacilityReviewTab(
    searchViewmodel: SearchKeywordViewmodel,
    isUser: Boolean,
    facilityId: Int,
    onReview: () -> Unit
) {
    LaunchedEffect(Unit) {
        searchViewmodel.getFacilityReview(facilityId)
    }
    val reviews by searchViewmodel.facilityReviews.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val hazeState = remember { HazeState() }

        if (isUser) {
            // if (리뷰를 단 적이 없으면) {
            // 리뷰 유도 박스
            FacilityReviewInduceItem(
                onClick = onReview
            )
            HorizontalDivider(thickness = 3.dp, color = Gray100)
            // }

            FacilityReviewInfo()
            HorizontalDivider(thickness = 3.dp, color = Gray100)

            FacilityReviews(
                reviews = reviews
            )
        } else {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .haze(
                            state = hazeState,
                            style = HazeMaterials.thick(containerColor = White)
                        )
                ) {
                    // if (리뷰를 단 적이 없으면) {
                    // 리뷰 유도 박스
                    FacilityReviewInduceItem()
                    HorizontalDivider(thickness = 3.dp, color = Gray100)
                    // }

                    FacilityReviewInfo()
                    HorizontalDivider(thickness = 3.dp, color = Gray100)

                    FacilityReviews(
                        reviews = emptyList()
                    )
                }
                BlockNonUser(
                    title = "리뷰",
                    hazeState = hazeState,
                )
            }
        }

    }
}


@Preview
@Composable
fun FacilityReviewInfo() {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 25.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "4.0",
            style = AppTypography.Bold_22.copy(color = Black)
        )
        Spacer(Modifier.width(4.dp))
        RateImage(
            width = 101.dp,
            height = 19.dp,
            rate = 0.8f
        )
    }
    
    // 나중에 ~하기 좋아요 추가한다고 하면 Column으로 감싸고 여기에 추가하면 됨
}

@Composable
fun FacilityReviewInduceItem(
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(25.dp)
            .clickable {
                onClick()
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "방문 후기를 남겨주세요.",
            style = AppTypography.Medium_18.copy(color = Black)
        )
        Spacer(Modifier.height(8.dp))
        RateImage(
            rate = 0f
        )
    }
}

/**
 * rate - 별점
 * 0 = 0 / 0.5 = 0.1 / 1 = 0.2 / 1.5 = 0.3 / 2 = 0.4
 * / 2.5 = 0.5 / 3 = 0.6 / 3.5 = 0.7 / 4 = 0.8 / 4.5 = 0.9 / 5 = 1.0
 *
 */
@Preview
@Composable
fun RateImage(
    width: Dp = 185.dp,
    height: Dp = 35.dp,
    rate: Float = 0.5f // 0.5 = 0.1 / 2.5 = 0.5 / 5.0 = 1
) {
    Box(
        modifier = Modifier.size(width, height)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Gray300)
        )

        Box(
            modifier = Modifier
                .size((width * rate), height)
                .background(PrimaryMid)
        )

        Image(
            painter = painterResource(R.drawable.ic_rate),
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize(),
            contentDescription = null
        )
    }
}