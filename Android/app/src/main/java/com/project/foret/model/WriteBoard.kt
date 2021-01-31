package com.project.foret.model

import java.io.Serializable

data class WriteBoard(
    val type: Int,
    val subject: String,
    val content: String,
) : Serializable