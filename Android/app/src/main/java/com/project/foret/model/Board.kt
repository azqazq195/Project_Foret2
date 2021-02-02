package com.project.foret.model

data class Board(
    val id: Long?,
    val foret_id: Long?,
    val type: Int?,
    val hit: Int?,
    val subject: String?,
    val content: String?,
    val reg_date: String?,
    val edit_date: String?,
    val member: Member?,
    val photos: MutableList<Photo>?,
    val comments: MutableList<Comment>?,
    val comment_count: Int?
) {
    constructor(id: Long)
            : this(id, null, null, null, null, null, null, null, null, null, null, null)
    constructor(foret_id: Long?, type: Int, subject: String, content: String, member: Member)
            : this(null,foret_id, type, null, subject, content, null, null, member, null, null, null)
}
