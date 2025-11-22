package com.hongildong.map.data.entity

data class FloorInfo(
    val floor: String,
    val facilities: List<FloorFacility>
)

data class FloorFacility(
    val id: Int,
    val name: String,
)

enum class Floor(val apiName: String, val displayName: String) {
    B4("B4", "지하 4층"),
    B3("B3", "지하 3층"),
    B2("B2", "지하 2층"),
    B1("B1", "지하 1층"),
    LOBBY("LOBBY", "로비층"),
    F1("F1", "1층"),
    F2("F2", "2층"),
    F3("F3", "3층"),
    F4("F4", "4층"),
    F5("F5", "5층"),
    F6("F6", "6층"),
    F7("F7", "7층"),
    F8("F8", "8층"),
    F9("F9", "9층"),
    F10("F10", "10층"),
    F11("F11", "11층"),
    F12("F12", "12층"),
    F13("F13", "13층"),
    F14("F14", "14층"),
    F15("F15", "15층"),
    F16("F16", "16층");

    companion object {
        // apiName 기반으로 enum 상수 검색
        fun fromApiName(apiName: String): Floor? {
            return Floor.entries.find { it.apiName.equals(apiName, ignoreCase = true) }
        }
        // displayName 기반으로 enum 상수 검색
        fun fromDisplayName(displayName: String): Floor? {
            return Floor.entries.find { it.displayName.equals(displayName, ignoreCase = true) }
        }
    }
}
