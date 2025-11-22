package com.hongildong.map.ui.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.hongildong.map.R

@Composable
fun NetworkImage(
    url: String,
    width: Dp = 170.dp,
    height: Dp = 190.dp,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = url,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(width, height)
            .clip(
                shape = RoundedCornerShape(15.dp)
            )
    )
}


@Composable
fun DummyImage(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(R.drawable.img_blank),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(
                width = 170.dp,
                height = 190.dp
            )
            .clip(
                shape = RoundedCornerShape(15.dp)
            )
            .padding(8.dp)
    )
}

@Composable
fun ProfileImage(
    profileUrl: String? = null,
    modifier: Modifier = Modifier.size(45.dp)
) {
    if (profileUrl.isNullOrEmpty()) {
        // 프로필 정해져있지 않으면 기본 프로필
        Image(
            painter = painterResource(R.drawable.img_empty_profile),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .clip(
                    shape = CircleShape
                )
        )
    } else {
        // 프로필 정해져 있으면 네트워크 이미지
        AsyncImage(
            model = profileUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .clip(
                    shape = CircleShape
                )
        )
    }
}