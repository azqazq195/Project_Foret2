package com.project.foret.model

import java.util.*

data class Foret (
    val id: Long?,
    val leader: Member?,
    val name: String?,
    val introduce: String?,
    val max_member: Int?,
    val reg_date: Date?,
    val photos: MutableList<Photo>?,
    val tags: MutableList<Tag>?,
    val regions: MutableList<Region>?
)