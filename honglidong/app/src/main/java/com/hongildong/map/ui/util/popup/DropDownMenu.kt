package com.hongildong.map.ui.util.popup

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hongildong.map.R
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Gray300
import com.hongildong.map.ui.theme.Gray500
import com.hongildong.map.ui.theme.White


@Composable
fun DropDownMenu(
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_dot_menu),
            contentDescription = "옵션 더보기",
            colorFilter = ColorFilter.tint(Gray500),
            modifier = Modifier
                .clickable {
                    expanded = !expanded
                }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = White,
            border = BorderStroke(width = 1.dp, color = Gray300)
        ) {
            DropdownMenuItem(
                trailingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_edit),
                        contentDescription = "수정",
                        tint = Gray500
                    )
                },
                text = {
                    Text(
                        text = "수정",
                        style = AppTypography.Medium_15.copy(color = Gray500)
                    )
                },
                onClick = {
                    onEdit()
                }
            )
            DropdownMenuItem(
                trailingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_delete),
                        contentDescription = "삭제",
                        tint = Gray500
                    )
                },
                text = {
                    Text(
                        text = "삭제",
                        style = AppTypography.Medium_15.copy(color = Gray500)
                    )
                },
                onClick = {
                    onDelete()
                }
            )
        }
    }
}