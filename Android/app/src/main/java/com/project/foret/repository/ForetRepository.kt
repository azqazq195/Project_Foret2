package com.project.foret.repository

import com.project.foret.api.RetrofitInstance

class ForetRepository {
    suspend fun getMyForets(member_id: Long) = RetrofitInstance.api.getMyForets(member_id)
    suspend fun getForetBoardList(foret_id: Long) = RetrofitInstance.api.getForetBoardList(foret_id)

    suspend fun getBoardDetails(board_id: Long) = RetrofitInstance.api.getBoardDetails(board_id)
    suspend fun getAnonymousBoardList(order: Int) = RetrofitInstance.api.getAnonymousBoardList(order)
}