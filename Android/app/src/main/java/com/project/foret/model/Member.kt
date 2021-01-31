package com.project.foret.model

import java.util.*

data class Member(
    val id: Long,
    val name: String?,
    val email: String?,
    val nickname: String?,
    val birth: Date?,
    val reg_date: Date?,
    val photos: MutableList<Photo>?,
    val tags: MutableList<Tag>?,
    val regions: MutableList<Region>?
) {
    constructor(id: Long)
            : this(id, null, null, null, null, null, null, null, null)
}
