package com.hongildong.map.ui.util

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Black
import com.hongildong.map.ui.theme.Gray300
import com.hongildong.map.ui.theme.Gray400
import com.hongildong.map.ui.theme.PrimaryLight
import com.hongildong.map.ui.theme.TypeEvent
import com.hongildong.map.ui.theme.White


// 커스텀 텍스트 필드
@Composable
fun CustomTextField(
    placeholderMessage: String,
    textState: String,
    onTextChange: (String) -> Unit,
    onSearch: (String) -> Unit = {},
    isPassword: Boolean = false,
    maxLength: Int = 20,
    textStyle: TextStyle = AppTypography.Regular_15.copy(color = Black)
) {
    // 키보드를 제어하기 위한 컨트롤러
    val keyboardController = LocalSoftwareKeyboardController.current

    BasicTextField(
        value = textState,
        onValueChange =  { if (it.length <= maxLength) onTextChange(it) },
        singleLine = true,
        textStyle = textStyle,
        visualTransformation = if (isPassword) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        modifier = Modifier.wrapContentSize(),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = White, shape = RoundedCornerShape(size = 10.dp))
                    .border(1.dp, color = Gray400, shape = RoundedCornerShape(size = 10.dp))
                    .padding(all = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // 텍스트 박스가 비어있을 경우 placeholder 메시지 출력
                if (textState.isEmpty()) {
                    Text(
                        text = placeholderMessage,
                        color = Gray400,
                        textAlign = TextAlign.Start,
                        style = AppTypography.Regular_15,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterVertically),
                    )
                } else {
                    innerTextField()
                }
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            // 완료 버튼을 눌렀을 때 실행될 동작
            onDone = {
                // onSearch 콜백 함수 실행
                onSearch(textState)
                // 검색 실행 후 키보드 숨기기
                keyboardController?.hide()
            }
        )
    )
}

@Composable
fun CustomUnderLineTextField(
    modifier: Modifier = Modifier,
    placeholderMessage: String,
    textState: String,
    onTextChange: (String) -> Unit,
    onEditDone: (String) -> Unit = {},
    isPassword: Boolean = false,
    maxLength: Int = 20,
    textStyle: TextStyle = AppTypography.Medium_15.copy(color = Black),
    suffix: String = ""
) {
    // 키보드를 제어하기 위한 컨트롤러
    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        value = textState,
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        onValueChange = {
            if (it.length <= maxLength) onTextChange(it)
        },
        visualTransformation = if (isPassword) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        colors = TextFieldDefaults.colors(
            focusedTextColor = Black,
            errorTextColor = TypeEvent,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            errorContainerColor = Color.Transparent,
            focusedIndicatorColor = PrimaryLight,
            unfocusedIndicatorColor = Gray400,
            errorIndicatorColor = TypeEvent,
            focusedPlaceholderColor = Gray300,
            unfocusedPlaceholderColor = Gray300,
            errorPlaceholderColor = TypeEvent,
            focusedSuffixColor = Black,
            unfocusedSuffixColor = Black,
        ),
        textStyle = textStyle,
        placeholder = {
            Text(placeholderMessage, style = textStyle.copy(color = Gray300))
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            // 완료 버튼을 눌렀을 때 실행될 동작
            onDone = {
                // onDone 콜백 함수 실행
                onEditDone(textState)
                // 검색 실행 후 키보드 숨기기
                keyboardController?.hide()
            }
        ),
        suffix = { if (suffix.isNotEmpty()) Text(suffix, style = AppTypography.Medium_15) }
    )
}
