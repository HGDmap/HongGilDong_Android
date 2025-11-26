package com.hongildong.map.ui.util

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.core.net.toUri
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Black
import com.hongildong.map.ui.theme.PrimaryMid

/**
 * 클릭하면 전화 앱(다이얼러)으로 이동하는 텍스트
 *
 * @param phoneNumber 전화번호 (예: "010-1234-5678")
 * @param modifier 수정자
 * @param style 텍스트 스타일 (기본값: Default)
 * @param color 텍스트 색상 (기본값: 파란색)
 */
@Composable
fun PhoneNumberText(
    phoneNumber: String,
    modifier: Modifier = Modifier,
    color: Color = PrimaryMid // 링크 느낌의 파란색
) {
    val context = LocalContext.current

    Text(
        text = phoneNumber,
        style = AppTypography.Medium_15.copy(
            color = color,
            textDecoration = TextDecoration.Underline
        ),
        modifier = modifier.clickable {
            // ACTION_DIAL: 전화 걸기 화면(다이얼러)으로 이동 (별도 권한 필요 없음)
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = "tel:$phoneNumber".toUri()
            }
            context.startActivity(intent)
        }
    )
}

/**
 * 클릭하면 웹사이트로 이동하는 텍스트
 *
 * @param url 이동할 웹사이트 주소 (예: "www.naver.com")
 * @param displayText 화면에 보여줄 텍스트 (생략 시 url이 그대로 보임)
 * @param modifier 수정자
 * @param style 텍스트 스타일
 * @param color 텍스트 색상
 */
@Composable
fun WebsiteText(
    url: String,
    displayText: String = url,
    modifier: Modifier = Modifier,
    color: Color = PrimaryMid
) {
    val context = LocalContext.current

    Text(
        text = displayText,
        style = AppTypography.Medium_15.copy(
            color = color,
            textDecoration = TextDecoration.Underline
        ),
        modifier = modifier.clickable {
            // http:// 또는 https://가 없으면 붙여줍니다.
            val validUrl = if (!url.startsWith("http://") && !url.startsWith("https://")) {
                "https://$url"
            } else {
                url
            }

            // ACTION_VIEW: 웹 브라우저 열기
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = validUrl.toUri()
            }
            // 앱이 크래시 나지 않도록 안전장치 (브라우저가 없는 경우 대비)
            try {
                context.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    )
}