package com.hongildong.map.ui.bookmark

import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hongildong.map.R
import com.hongildong.map.data.entity.FolderColor
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Black
import com.hongildong.map.ui.theme.Gray400
import com.hongildong.map.ui.theme.Gray500
import com.hongildong.map.ui.util.CustomTextField
import kotlinx.coroutines.launch

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

@Composable
fun BookmarkFolderUpdateContent(
    onClose: () -> Unit
) {
    var textState by remember { mutableStateOf("") }

    Column {
        Row {
            Text(
                "새 리스트",
                style = AppTypography.Bold_22.copy(color = Black),
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Image(
                painter = painterResource(R.drawable.ic_close),
                contentDescription = "리스트 만들기 취소하기",
                modifier = Modifier
                    .size(20.dp)
                    .clickable {
                        onClose()
                    }
            )
        }

        CustomTextField(
            placeholderMessage = "폴더명을 입력해주세요",
            textState = textState,
            onTextChange = { textState = it },
            onSearch = {},
            textStyle = AppTypography.Bold_20.copy(color = Black)
        )
        Text(
            "색상 선택",
            style = AppTypography.Medium_18.copy(color = Black),
        )
        Row {
            FolderColor.entries.forEach {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .background(it.color)
                )
            }
        }
    }
}