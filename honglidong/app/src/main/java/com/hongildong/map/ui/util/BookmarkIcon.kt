package com.hongildong.map.ui.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hongildong.map.R
import com.hongildong.map.ui.theme.BookmarkBlue


@Composable
fun BookmarkIcon(
    size: Dp,
    color: Color,
) {
    Image(
        painter = painterResource(R.drawable.ic_bookmark_heart),
        contentDescription = null,
        modifier = Modifier
            .clip(CircleShape)
            .background(color)
            .padding(5.dp)
            .size(size)
    )
}