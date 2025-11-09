package com.hongildong.map.ui.bookmark

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hongildong.map.R
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Black
import com.hongildong.map.ui.theme.Gray400
import com.hongildong.map.ui.theme.Gray500
import com.hongildong.map.ui.util.BookmarkIcon

// 홈 - 북마크의 폴더 리스트 아이템
@Composable
fun BookmarkFolderItem(
    folderColor: Color,
    folderName: String,
    numOfItem: Int,
    onClick: () -> Unit = {}
) {
    Column (
        modifier = Modifier.fillMaxWidth().clickable { onClick() }
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 10.dp)
        ) {
            BookmarkIcon(
                19.dp,
                folderColor
            )
            Spacer(Modifier.width(10.dp))
            Column (
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    folderName,
                    style = AppTypography.Medium_18.copy(color = Black)
                )
                Spacer(Modifier.height(3.dp))
                Row {
                    Image(
                        painter = painterResource(R.drawable.ic_nearby),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(Gray500),
                        modifier = Modifier
                            .size(14.dp)
                    )
                    Spacer(Modifier.width(3.dp))
                    Text(
                        "$numOfItem",
                        style = AppTypography.Medium_13.copy(Gray500)
                    )
                }
            }
            Image(
                painter = painterResource(R.drawable.ic_dot_menu),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Gray500),
                modifier = Modifier
            )
        }
        HorizontalDivider(thickness = 1.dp, color = Gray400)
    }
}