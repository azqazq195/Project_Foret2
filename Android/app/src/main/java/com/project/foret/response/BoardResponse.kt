package com.project.foret.response

import com.project.foret.model.Board

data class BoardResponse(
    val total: Int,
    val boards: MutableList<Board>
)
