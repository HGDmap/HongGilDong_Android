package com.hongildong.map.ui.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.hongildong.map.R

@Composable
fun NetworkImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = url,
        contentDescription = contentDescription,
        modifier = modifier
            .size(
                width = 170.dp,
                height = 190.dp
            )
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
        contentScale = ContentScale.FillBounds,
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