package com.project.foret.model

data class Board(
    val id: Long,
    val writer_id: Long,
    val foret_id: Long,
    val type: Int,
    val hit: Int,
    val subject: String,
    val content: String,
    val reg_date: String,
    val edit_date: String,
    val photos: MutableList<Photo>
)
