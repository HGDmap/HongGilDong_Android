package com.hongildong.map.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.White

@Composable
fun ProfileScreen() {
    Box(
        modifier = Modifier.fillMaxSize().background(White),
        contentAlignment = Alignment.Center
    ) {
        Text("마이페이지", style = AppTypography.Regular_18)
    }

}
