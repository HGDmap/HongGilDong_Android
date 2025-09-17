package com.hongildong.map.ui.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hongildong.map.R
import com.hongildong.map.ui.theme.AppTypography

@Composable
fun HeaderWithGoBack(
    onGoBackClick: () -> Unit,
    title: String
) {
    Box (
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            title,
            style = AppTypography.Bold_22,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp)
        )
        Image(
            painter = painterResource(R.drawable.ic_back),
            contentDescription = stringResource(R.string.go_back),
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickable { onGoBackClick() }
        )
    }
}
