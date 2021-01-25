package com.project.foret.api

import com.project.foret.response.BoardResponse
import com.project.foret.response.ForetResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ForetAPI {
    @GET("/foret/myForets")
    suspend fun getMyForets(
        @Query("member_id")
        member_id: Long
    ) : Response<ForetResponse>

    @GET("/board/getForetBoard")
    suspend fun getForetBoard(
        @Query("foret_id")
        foret_id: Long
    ) : Response<BoardResponse>
}