package com.hongildong.map.ui.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hongildong.map.R
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Gray500
import com.hongildong.map.ui.theme.HongildongTheme

@Composable
fun EmptyContents(
    message: String
) {
    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 비어있을 때 홍길동 이미지
        Image(
            painter = painterResource(R.drawable.ic_empty_contents),
            contentDescription = null,
            modifier = Modifier.width(110.dp).height(150.dp),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = AppTypography.Bold_13.copy(color = Gray500),
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun EmptyContentsPreview() {
    HongildongTheme {
        EmptyContents(
            "등록된 리뷰가 아직 없어요."
        )
    }
}