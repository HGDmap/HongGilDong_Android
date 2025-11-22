package com.hongildong.map.ui.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hongildong.map.R
import com.hongildong.map.ui.theme.TypeEvent
import com.hongildong.map.ui.theme.White


@Composable
fun BookmarkIcon(
    size: Dp,
    color: Color,
) {
    Box(
        modifier = Modifier
            .padding(5.dp)
            .background(
                color = color,
                shape = CircleShape
            )
            .padding(5.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_bookmark_heart),
            contentDescription = null,
            modifier = Modifier
                .size(size)
        )
    }
}

@Composable
fun SearchResultIcon(
    size: Dp,
    icon: Int = 0
) {
    Box(
        modifier = Modifier
            .padding(5.dp)
            .background(
                color = TypeEvent,
                shape = CircleShape
            )
            .padding(5.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_location_info),
            contentDescription = null,
            colorFilter = ColorFilter.tint(White),
            modifier = Modifier
                .size(size)
        )
    }
}