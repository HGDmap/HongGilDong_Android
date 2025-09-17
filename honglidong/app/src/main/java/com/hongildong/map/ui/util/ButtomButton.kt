package com.hongildong.map.ui.util

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Gray300
import com.hongildong.map.ui.theme.Gray400
import com.hongildong.map.ui.theme.PrimaryMid
import com.hongildong.map.ui.theme.White

@Composable
fun BottomButton(
    buttonText: String,
    isButtonEnabled: Boolean,
    onClick: () -> Unit,
) {
    val buttonShape = RoundedCornerShape(10.dp)

    Button(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .defaultMinSize(minHeight = 50.dp)
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = if(isButtonEnabled) PrimaryMid else Gray400,
                shape = buttonShape
            ),
        shape = buttonShape,
        colors = ButtonColors(
            containerColor = PrimaryMid,
            contentColor = White,
            disabledContainerColor = White,
            disabledContentColor = Gray300,
        ),
        onClick = {
            onClick()
        },
        enabled = isButtonEnabled
    ) {
        Text(
            buttonText,
            style = AppTypography.Bold_18
        )
    }
}

@Composable
fun SmallButton(
    buttonText: String,
    isButtonEnabled: Boolean,
    onClick: () -> Unit,
) {
    val buttonShape = RoundedCornerShape(10.dp)

    Button(
        modifier = Modifier
            .padding(start = 10.dp)
            .defaultMinSize(minHeight = 47.dp)
            .border(
                width = 1.dp,
                color = if(isButtonEnabled) PrimaryMid else Gray400,
                shape = buttonShape
            ),
        contentPadding = PaddingValues(8.dp),
        shape = buttonShape,
        colors = ButtonColors(
            containerColor = PrimaryMid,
            contentColor = White,
            disabledContainerColor = White,
            disabledContentColor = Gray300,
        ),
        onClick = {
            onClick()
        },
        enabled = isButtonEnabled
    ) {
        Text(
            buttonText,
            style = AppTypography.Bold_18
        )
    }
}