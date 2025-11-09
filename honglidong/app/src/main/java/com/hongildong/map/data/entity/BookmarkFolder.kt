package com.hongildong.map.data.entity

data class BookmarkFolder (
    val folderId: Int,
    val folderName: String,
    val color: String,
    val bookmarkList: List<NodeInfo>,
    val bookmarkCount: Int
)

enum class FolderColor(colorName: String) {
    RED("red"),
    ORANGE("orange"),
    YELLOW("yellow"),
    GREEN("green"),
    MINT("mint"),
    BLUE("blue"),
    PINK("pink"),
    PURPLE("purple")
}