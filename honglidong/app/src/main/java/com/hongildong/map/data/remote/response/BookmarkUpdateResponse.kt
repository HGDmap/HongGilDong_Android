package com.hongildong.map.data.remote.response

import com.hongildong.map.data.entity.NodeInfo

data class BookmarkUpdateResponse (
    val folderId: Int,
    val folderName: String,
    val color: String,
    val bookmarkList: List<NodeInfo>,
    val bookmarkCount: Int
)