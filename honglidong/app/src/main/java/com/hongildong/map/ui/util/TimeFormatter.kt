package com.hongildong.map.ui.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

// 생성/수정 날짜 데이터 포매팅 yyyy.MM.dd 형태
fun formatDate(isoString: String): String {
    return try {
        val parsedDate = LocalDateTime.parse(isoString)
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        outputFormatter.format(parsedDate)
    } catch (e: DateTimeParseException) {
        isoString
    }
}