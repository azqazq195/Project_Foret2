package com.project.foret.api

import androidx.annotation.Nullable
import com.project.foret.model.Board
import com.project.foret.model.Comment
import com.project.foret.model.WriteBoard
import com.project.foret.response.BoardListResponse
import com.project.foret.response.CreateResponse
import com.project.foret.response.ForetResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ForetAPI {
    @GET("/foret/myForets")
    suspend fun getMyForets(
        @Query("member_id")
        member_id: Long
    ) : Response<ForetResponse>

    @GET("/board/getForetBoardList")
    suspend fun getForetBoardList(
        @Query("foret_id")
        foret_id: Long
    ) : Response<BoardListResponse>

    @GET("/board/getAnonymousBoardList/{order}")
    suspend fun getAnonymousBoardList(
        @Path("order")
        order: Int
    ) : Response<BoardListResponse>

    @GET("/board/details/{board_id}")
    suspend fun getBoardDetails(
        @Path("board_id")
        board_id: Long
    ) : Response<Board>

    @POST("/board/create")
    suspend fun createBoard(
        @Query("member_id")
        member_id: Long,
        @Body
        board: WriteBoard
    ) : Response<CreateResponse>

    @POST("/comment/create")
    suspend fun createComment(
        @Query("member_id")
        member_id: Long,
        @Query("board_id")
        board_id: Long,
        @Query("id")
        @Nullable id: Long,
        @Body
        comment: Comment
    ) : Response<CreateResponse>
}