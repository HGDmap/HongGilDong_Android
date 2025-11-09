package com.hongildong.map.ui.bookmark

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hongildong.map.R
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Black
import com.hongildong.map.ui.theme.Gray400
import com.hongildong.map.ui.theme.Gray500

// 홈 - 북마크의 화면 헤더 (리스트 개수와 새 리스트 만들기 부분)
@Composable
fun BookmarkFolderHeader(
    numOfFolder: Int,
    addFolder: () -> Unit
) {
    Column {
        Text("리스트 $numOfFolder", style = AppTypography.Bold_20)
        Spacer(Modifier.height(20.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    addFolder()
                }
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_add_folder),
                contentDescription = null,
                tint = Gray500,
                modifier = Modifier.padding(8.dp)
            )
            Text("새 리스트 만들기", style = AppTypography.Medium_18.copy(color = Gray500))
        }
        HorizontalDivider(thickness = 1.dp, color = Gray400)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkUpdateBottomSheet() {
    ModalBottomSheet(
        onDismissRequest = {}
    ) {
        Column {
            Row {
                Text(
                    "새 리스트",
                    style = AppTypography.Bold_22.copy(color = Black)
                )
            }
        }
    }
}