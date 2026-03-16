package com.hongildong.map.ui.search.location_detail.facility.review

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hongildong.map.R
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Black
import com.hongildong.map.ui.theme.PrimaryMid


@Composable
fun LikedReviewIcon(
    likedCnt: Int,
    isLiked: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .border(width = 1.dp, color = if (isLiked) PrimaryMid else Black, shape = RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(if (isLiked) R.drawable.ic_liked_true else R.drawable.ic_liked_false),
            contentDescription = null,
            colorFilter = ColorFilter.tint(if (isLiked) PrimaryMid else Black),
            modifier = Modifier.size(16.dp)
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = likedCnt.toString(),
            style = AppTypography.Medium_18.copy(color = if (isLiked) PrimaryMid else Black)
        )
    }
}