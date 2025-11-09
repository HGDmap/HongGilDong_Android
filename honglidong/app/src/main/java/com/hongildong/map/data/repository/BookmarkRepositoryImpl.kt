package com.hongildong.map.data.repository

import com.hongildong.map.data.remote.api.BookmarkService
import com.hongildong.map.data.remote.request.BookmarkFolderUpdateRequest
import com.hongildong.map.data.remote.response.BookmarkAllResponse
import com.hongildong.map.data.entity.BookmarkFolder
import com.hongildong.map.data.util.DefaultResponse
import com.hongildong.map.data.util.safeApiCall
import javax.inject.Inject

class BookmarkRepositoryImpl @Inject constructor(
    private val api: BookmarkService
): BookmarkRepository {
    override suspend fun getAllBookmarks(accessToken: String): DefaultResponse<BookmarkAllResponse> {
        return safeApiCall { api.getAllBookmarks(accessToken) }
    }

    override suspend fun update(
        accessToken: String,
        type: String,
        folderId: Int,
        targetId: Int
    ): DefaultResponse<BookmarkFolder> {
        return safeApiCall { api.update(accessToken, type, folderId, targetId) }
    }

    override suspend fun delete(
        accessToken: String,
        type: String,
        targetId: Int
    ): DefaultResponse<BookmarkFolder> {
        return safeApiCall { api.delete(accessToken, type, targetId) }
    }

    override suspend fun addFolder(
        accessToken: String,
        body: BookmarkFolderUpdateRequest
    ): DefaultResponse<BookmarkAllResponse> {
        return safeApiCall { api.addFolder(accessToken, body) }
    }

    override suspend fun updateFolder(
        accessToken: String,
        folderId: Int,
        body: BookmarkFolderUpdateRequest
    ): DefaultResponse<BookmarkAllResponse> {
        return safeApiCall { api.updateFolder(accessToken, folderId, body) }
    }

    override suspend fun deleteFolder(
        accessToken: String,
        folderId: Int
    ): DefaultResponse<BookmarkAllResponse> {
        return safeApiCall { api.deleteFolder(accessToken, folderId) }
    }

    override suspend fun getBookmarksOfFolder(
        accessToken: String,
        folderId: Int
    ): DefaultResponse<BookmarkFolder> {
        return safeApiCall { api.getBookmarksOfFolder(accessToken, folderId) }
    }

}