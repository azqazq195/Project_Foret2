package com.project.foret.api

import com.project.foret.model.Board
import com.project.foret.model.Comment
import com.project.foret.model.Foret
import com.project.foret.response.BoardListResponse
import com.project.foret.response.CommentsResponse
import com.project.foret.response.CreateResponse
import com.project.foret.response.ForetResponse
import retrofit2.Response
import retrofit2.http.*

interface ForetAPI {
    @GET("/foret/myForets")
    suspend fun getMyForets(
        @Query("member_id")
        member_id: Long
    ) : Response<ForetResponse>

    @GET("/board/getHomeBoardList")
    suspend fun getHomeBoardList(
        @Query("foret_id")
        foret_id: Long
    ) : Response<BoardListResponse>

    @GET("/board/getForetBoardList")
    suspend fun getForetBoardList(
        @Query("foret_id")
        foret_id: Long,
        @Query("type")
        type: Int
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

    @GET("/foret/details/{foret_id}")
    suspend fun getForetDetails(
        @Path("foret_id")
        foret_id: Long
    ) : Response<Foret>

    @GET("/comment/getComments")
    suspend fun getComments(
        @Query("board_id")
        board_id: Long
    ) : Response<CommentsResponse>

    @POST("/board/create")
    suspend fun createBoard(
        @Body
        board: Board
    ) : Response<CreateResponse>

    @POST("/comment/create")
    suspend fun createComment(
        @Body
        comment: Comment
    ) : Response<CreateResponse>
}