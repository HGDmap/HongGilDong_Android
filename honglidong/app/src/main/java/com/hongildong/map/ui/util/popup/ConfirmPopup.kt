package com.hongildong.map.ui.util.popup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Black
import com.hongildong.map.ui.theme.Gray300
import com.hongildong.map.ui.theme.PrimaryMid
import com.hongildong.map.ui.theme.White

@Composable
fun ConfirmPopup(
    message: String,
    dismissMsg: String,
    confirmMsg: String,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {

        Card(
            modifier = Modifier
                .heightIn(min = 130.dp, max = 160.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = message,
                    style = AppTypography.Bold_18.copy(color = Black),
                    modifier = Modifier.padding(vertical = 20.dp),
                )
                HorizontalDivider(thickness = 1.dp, color = Gray300)
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(2.dp).weight(1f),
                    ) {
                        Text(
                            text = dismissMsg,
                            style = AppTypography.Medium_18.copy(color = PrimaryMid)
                        )
                    }
                    VerticalDivider(thickness = 1.dp, color = Gray300)
                    TextButton(
                        onClick = { onConfirmation() },
                        modifier = Modifier.padding(2.dp).weight(1f),
                    ) {
                        Text(
                            text = confirmMsg,
                            style = AppTypography.Medium_18.copy(color = PrimaryMid)
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun ConfirmPopUpPreview() {
    ConfirmPopup(
        message = "이 리스트를 삭제합니다.",
        dismissMsg = "취소",
        confirmMsg = "삭제",
        onDismissRequest = {},
        onConfirmation = {}
    )
}