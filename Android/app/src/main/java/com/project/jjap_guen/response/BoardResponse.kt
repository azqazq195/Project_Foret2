package com.project.jjap_guen.response

import com.project.jjap_guen.model.Board

data class BoardResponse(
    val total: Int,
    val boards: MutableList<Board>
)
