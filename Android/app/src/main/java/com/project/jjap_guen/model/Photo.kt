package com.project.jjap_guen.model

import java.util.*

data class Photo(
    val dir: String,
    val filename: String,
    val originname: String,
    val filesize: Int,
    val filetype: String,
    val reg_date: Date
)
