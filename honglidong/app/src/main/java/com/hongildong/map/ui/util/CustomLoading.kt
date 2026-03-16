package com.hongildong.map.ui.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hongildong.map.ui.theme.PrimaryMid
import com.hongildong.map.ui.theme.White

@Composable
fun CustomLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(50.dp),
            color = PrimaryMid,
            trackColor = Color.Transparent,
            strokeWidth = 2.dp
        )
    }

}

@Preview
@Composable
fun CustomLoadingPreview() {
    CustomLoading()
}