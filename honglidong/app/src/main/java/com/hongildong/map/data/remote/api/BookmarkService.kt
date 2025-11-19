package com.hongildong.map.data.remote.api

import com.hongildong.map.data.remote.request.BookmarkFolderUpdateRequest
import com.hongildong.map.data.remote.response.BookmarkAllResponse
import com.hongildong.map.data.entity.BookmarkFolder
import com.hongildong.map.data.util.ApiResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface BookmarkService {

    // 개별 북마크

    // 전체 북마크 현황 받아오기
    @GET("bookmarks/all")
    suspend fun getAllBookmarks(
        @Header("Authorization") accessToken: String,
    ): ApiResponse<BookmarkAllResponse>

    // 북마크 추가 및 수정
    @POST("bookmarks/{type}")
    suspend fun update(
        @Header("Authorization") accessToken: String,
        @Path("type") type: String, // BUILDING or FACILITY
        @Query("folderId") folderId: Int,
        @Query("targetId") targetId: Int
    ): ApiResponse<BookmarkFolder>

    // 북마크 삭제
    // 북마크 수정시 아무 폴더도 선택하지 않고 확인 누를 경우
    @DELETE("bookmarks/{type}")
    suspend fun delete(
        @Header("Authorization") accessToken: String,
        @Path("type") type: String, // BUILDING or FACILITY
        @Query("targetId") targetId: Int
    ): ApiResponse<BookmarkFolder>

    // 북마크 폴더

    // 북마크 폴더 추가
    @POST("bookmarks/folders")
    suspend fun addFolder(
        @Header("Authorization") accessToken: String,
        @Body body: BookmarkFolderUpdateRequest
    ): ApiResponse<BookmarkAllResponse>

    // 북마크 폴더 수정
    @PUT("bookmarks/folders/{folderId}")
    suspend fun updateFolder(
        @Header("Authorization") accessToken: String,
        @Path("folderId") folderId: Int,
        @Body body: BookmarkFolderUpdateRequest
    ): ApiResponse<BookmarkAllResponse>

    // 북마크 폴더 삭제
    @DELETE("bookmarks/folders/{folderId}")
    suspend fun deleteFolder(
        @Header("Authorization") accessToken: String,
        @Path("folderId") folderId: Int,
    ): ApiResponse<BookmarkAllResponse>

    // 단일 북마크 폴더 조회
    @GET("bookmarks/folders/{folderId}")
    suspend fun getBookmarksOfFolder(
        @Header("Authorization") accessToken: String,
        @Path("folderId") folderId: Int,
    ): ApiResponse<BookmarkFolder>
}