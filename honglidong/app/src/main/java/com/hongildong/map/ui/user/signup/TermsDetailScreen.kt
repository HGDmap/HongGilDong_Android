package com.hongildong.map.ui.user.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hongildong.map.R
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Gray400
import com.hongildong.map.ui.theme.PrimaryMid
import com.hongildong.map.ui.theme.White
import com.hongildong.map.ui.util.HeaderWithGoBack

@Composable
fun TermsDetailScreen(
    term: Term,
    onGoBackClick: () -> Unit,
    onAgreeClick: (Term) -> Unit
) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(White),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        HeaderWithGoBack(
            onGoBackClick = onGoBackClick,
            title = stringResource(R.string.terms)
        )

        Text(term.title, style = AppTypography.Bold_24)
        HorizontalDivider(thickness = 1.dp, color = Gray400)
        Text(term.content, style = AppTypography.Medium_18)

        Button(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonColors(
                containerColor = PrimaryMid,
                disabledContainerColor = PrimaryMid,
                contentColor = White,
                disabledContentColor = White
            ),
            onClick = { onAgreeClick(term) }
        ) {
            Text(
                text = stringResource(R.string.continue_button),
                style = AppTypography.Bold_18.copy(color = White)
            )
        }
    }
}