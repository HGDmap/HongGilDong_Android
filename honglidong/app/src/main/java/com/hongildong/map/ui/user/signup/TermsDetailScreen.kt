package com.hongildong.map.ui.user.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hongildong.map.R
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Gray400
import com.hongildong.map.ui.theme.White
import com.hongildong.map.ui.util.BottomButton
import com.hongildong.map.ui.util.HeaderWithGoBack

@Composable
fun TermsDetailScreen(
    termId: Int,
    viewmodel: TermsViewmodel = viewModel(),
    onGoBackClick: () -> Unit,
    onAgreeClick: () -> Unit
) {
    val terms by viewmodel.terms.collectAsState()
    val term = terms[termId]

    Column (
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(20.dp)
            .background(White),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        HeaderWithGoBack(
            onGoBackClick = onGoBackClick,
            title = stringResource(R.string.terms)
        )

        Column {
            Text(term.title, style = AppTypography.Bold_22)
            HorizontalDivider(thickness = 1.dp, color = Gray400, modifier = Modifier.padding(vertical = 20.dp))
            Text(term.content, style = AppTypography.Medium_15)
        }

        BottomButton(
            buttonText = "동의하고 가입하기",
            isButtonEnabled = true,
            onClick = {
                viewmodel.onTermCheckedChange(termId, true)
                onAgreeClick()
            }
        )
    }
}