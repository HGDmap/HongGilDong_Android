package com.hongildong.map.data.entity

import androidx.compose.ui.graphics.Color
import com.hongildong.map.ui.theme.BookmarkBlue
import com.hongildong.map.ui.theme.BookmarkGreen
import com.hongildong.map.ui.theme.BookmarkMint
import com.hongildong.map.ui.theme.BookmarkOrange
import com.hongildong.map.ui.theme.BookmarkPink
import com.hongildong.map.ui.theme.BookmarkPurple
import com.hongildong.map.ui.theme.BookmarkRed
import com.hongildong.map.ui.theme.BookmarkYellow

data class BookmarkFolder (
    val folderId: Int,
    val folderName: String,
    val color: String,
    val bookmarkList: List<BookmarkInfo>,
    val bookmarkCount: Int
)

enum class FolderColor(val colorName: String, val color: Color) {
    RED("red", BookmarkRed),
    ORANGE("orange", BookmarkOrange),
    YELLOW("yellow", BookmarkYellow),
    GREEN("green", BookmarkGreen),
    MINT("mint", BookmarkMint),
    BLUE("blue", BookmarkBlue),
    PINK("pink", BookmarkPink),
    PURPLE("purple", BookmarkPurple);

    companion object {
        // colorName 기반으로 enum 상수 검색
        fun fromColorName(colorName: String): FolderColor? {
            return FolderColor.entries.find { it.colorName.equals(colorName, ignoreCase = true) }
        }
        // color를 기반으로 enum 상수 검색
        fun fromColor(color: Color): FolderColor? {
            return FolderColor.entries.find { it.color == color }
        }
    }
}