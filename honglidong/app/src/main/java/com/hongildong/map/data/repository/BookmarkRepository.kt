package com.hongildong.map.data.repository

import com.hongildong.map.data.remote.request.BookmarkFolderUpdateRequest
import com.hongildong.map.data.remote.response.BookmarkAllResponse
import com.hongildong.map.data.remote.response.BookmarkUpdateResponse
import com.hongildong.map.data.util.DefaultResponse

interface BookmarkRepository {
    suspend fun getAllBookmarks(
        accessToken: String
    ): DefaultResponse<BookmarkAllResponse>

    suspend fun update(
        accessToken: String,
        type: String,
        folderId: Int,
        targetId: Int
    ): DefaultResponse<BookmarkUpdateResponse>

    suspend fun delete(
        accessToken: String,
        type: String,
        targetId: Int
    ): DefaultResponse<BookmarkUpdateResponse>

    suspend fun addFolder(
        accessToken: String,
        body: BookmarkFolderUpdateRequest
    ): DefaultResponse<BookmarkAllResponse>

    suspend fun updateFolder(
        accessToken: String,
        folderId: Int,
        body: BookmarkFolderUpdateRequest
    ): DefaultResponse<BookmarkAllResponse>

    suspend fun deleteFolder(
        accessToken: String,
        folderId: Int
    ): DefaultResponse<BookmarkAllResponse>

    suspend fun getBookmarksOfFolder(
        accessToken: String,
        folderId: Int
    ): DefaultResponse<BookmarkUpdateResponse>
}