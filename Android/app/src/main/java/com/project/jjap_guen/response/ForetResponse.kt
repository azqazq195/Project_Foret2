package com.project.jjap_guen.response

import com.project.jjap_guen.model.Foret

data class ForetResponse (
    val total: Int,
    val forets: MutableList<Foret>
)