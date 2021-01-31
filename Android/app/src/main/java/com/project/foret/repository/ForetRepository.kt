package com.project.foret.repository

import android.util.Log
import com.project.foret.api.RetrofitInstance
import com.project.foret.model.Board
import com.project.foret.model.WriteBoard
import com.project.foret.response.CreateResponse
import retrofit2.Response

class ForetRepository {
    suspend fun getMyForets(member_id: Long) = RetrofitInstance.api.getMyForets(member_id)
    suspend fun getForetBoardList(foret_id: Long) = RetrofitInstance.api.getForetBoardList(foret_id)

    suspend fun getBoardDetails(board_id: Long) = RetrofitInstance.api.getBoardDetails(board_id)
    suspend fun getAnonymousBoardList(order: Int) = RetrofitInstance.api.getAnonymousBoardList(order)

    suspend fun createBoard(member_id: Long, board: WriteBoard): Response<CreateResponse> {
        return RetrofitInstance.api.createBoard(member_id, board)
    }
}