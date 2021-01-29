package com.project.foret.api

import com.project.foret.model.Board
import com.project.foret.response.BoardListResponse
import com.project.foret.response.ForetResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

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
}