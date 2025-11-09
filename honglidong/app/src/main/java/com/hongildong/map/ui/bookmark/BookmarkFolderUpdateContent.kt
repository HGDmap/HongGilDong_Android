package com.hongildong.map.ui.bookmark

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hongildong.map.R
import com.hongildong.map.data.entity.FolderColor
import com.hongildong.map.data.remote.request.BookmarkFolderUpdateRequest
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Black
import com.hongildong.map.ui.theme.Gray400
import com.hongildong.map.ui.theme.White
import com.hongildong.map.ui.util.BottomButton
import com.hongildong.map.ui.util.CustomTextField

@Composable
fun BookmarkFolderUpdateContent(
    onDone: (BookmarkFolderUpdateRequest) -> Unit
) {
    var textState by remember { mutableStateOf("") }
    var colorState by remember { mutableStateOf("") }

    Column (
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Text(
            "새 리스트",
            style = AppTypography.Bold_20.copy(color = Black),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(20.dp))
        CustomTextField(
            placeholderMessage = "폴더명을 입력해주세요",
            textState = textState,
            onTextChange = { textState = it },
            onSearch = {},
            textStyle = AppTypography.Bold_20.copy(color = Black)
        )
        Spacer(Modifier.height(14.dp))
        Text(
            "색상 선택",
            style = AppTypography.Medium_18.copy(color = Black),
        )
        Spacer(Modifier.height(8.dp))
        Row {
            FolderColor.entries.forEach {
                Box(
                    modifier = Modifier
                        .padding(5.dp)
                        .background(
                            color = if(it.colorName == colorState) it.color.copy(alpha = 0.3f) else Color.Transparent,
                            shape = CircleShape
                        )
                        .padding(5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .background(
                                it.color,
                                shape = CircleShape
                            )
                            .clickable {
                                colorState = it.colorName
                            }
                            .align(Alignment.Center)
                    ) {
                        if (it.colorName == colorState) {
                            Icon(
                                painter = painterResource(R.drawable.ic_selected_color),
                                contentDescription = null,
                                tint = White,
                                modifier = Modifier
                                    .align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        HorizontalDivider(thickness = 1.dp, color = Gray400)
        Spacer(Modifier.height(30.dp))

        BottomButton(
            buttonText = "완료",
            isButtonEnabled = if (textState.isNotEmpty() and colorState.isNotEmpty()) true else false,
            onClick = {
                onDone(
                    BookmarkFolderUpdateRequest(
                        folderName = textState,
                        folderColor = colorState
                    )
                )
            }
        )
    }
}