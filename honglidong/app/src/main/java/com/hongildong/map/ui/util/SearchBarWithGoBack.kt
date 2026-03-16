package com.hongildong.map.ui.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hongildong.map.R
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Black
import com.hongildong.map.ui.theme.Gray400
import com.hongildong.map.ui.theme.White

@Composable
fun SearchBarWithGoBack(
    searchedWord: String,
    onGoBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .shadow(3.dp)
            .background(White)
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(top = 16.dp, start = 10.dp, end = 10.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.ic_back),
            contentDescription = stringResource(R.string.go_back),
            modifier = Modifier
                .clickable {
                    onGoBack()
                }
        )
        Spacer(
            modifier = Modifier.width(15.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(53.dp)
                .background(color = White, shape = RoundedCornerShape(size = 10.dp))
                .border(1.dp, color = Gray400, shape = RoundedCornerShape(size = 10.dp)),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = searchedWord,
                color = Black,
                style = AppTypography.Regular_15,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}