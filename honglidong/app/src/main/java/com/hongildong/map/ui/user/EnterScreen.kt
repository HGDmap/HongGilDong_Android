package com.hongildong.map.ui.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hongildong.map.R
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Gray500
import com.hongildong.map.ui.theme.Gray600
import com.hongildong.map.ui.theme.White
import com.hongildong.map.ui.util.BottomButton

@Composable
fun EnterScreen(
    onStrangerClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .systemBarsPadding()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column (
            modifier = Modifier.fillMaxWidth().weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )
            Spacer(Modifier.height(20.dp))
            Text(
                "홍대의 길잃은 동문들 위한 지도",
                style = AppTypography.Bold_20
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "내 강의실부터 학교 이벤트까지,\n지금 길을 바로 찾아보세요!",
                style = AppTypography.Medium_15.copy(color = Gray600),
                textAlign = TextAlign.Center
            )
        }

        Column (
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            BottomButton(
                buttonText = "홍대생으로 시작하기",
                isButtonEnabled = true,
                onClick = onLoginClick
            )
            Spacer(Modifier.height(10.dp))
            Text(
                "비회원으로 계속하기",
                style = AppTypography.Medium_15.copy(color = Gray500),
                modifier = Modifier
                    .clickable {
                        onStrangerClick()
                    }
            )
        }
    }
}