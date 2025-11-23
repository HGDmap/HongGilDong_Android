package com.hongildong.map.ui.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

// 컬렉션 생성/수정 날짜 데이터 포매팅 yyyy.MM.dd 형태
fun formatDate(isoString: String): String {
    return try {
        val inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        val date = LocalDate.parse(isoString, inputFormatter)
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        outputFormatter.format(date)
    } catch (e: DateTimeParseException) {
        isoString
    }
}