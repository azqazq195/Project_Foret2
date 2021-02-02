package com.project.foret.repository

import com.project.foret.api.RetrofitInstance
import com.project.foret.model.Board
import com.project.foret.model.Comment
import com.project.foret.response.CreateResponse
import retrofit2.Response

class ForetRepository {
    suspend fun getMyForets(member_id: Long) =
        RetrofitInstance.api.getMyForets(member_id)

    suspend fun getHomeBoardList(foret_id: Long) =
        RetrofitInstance.api.getHomeBoardList(foret_id)

    suspend fun getForetBoardList(foret_id: Long, type: Int) =
        RetrofitInstance.api.getForetBoardList(foret_id, type)

    suspend fun getForetDetails(foret_id: Long) =
        RetrofitInstance.api.getForetDetails(foret_id)

    suspend fun getBoardDetails(board_id: Long) =
        RetrofitInstance.api.getBoardDetails(board_id)

    suspend fun getAnonymousBoardList(order: Int) =
        RetrofitInstance.api.getAnonymousBoardList(order)

    suspend fun getComments(board_id: Long) =
        RetrofitInstance.api.getComments(board_id)

    suspend fun createBoard(board: Board): Response<CreateResponse> =
        RetrofitInstance.api.createBoard(board)

    suspend fun createComment(comment: Comment): Response<CreateResponse> =
        RetrofitInstance.api.createComment(comment)

}