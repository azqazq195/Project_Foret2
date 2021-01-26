package com.project.foret.model

import android.provider.ContactsContract
import java.util.*

data class Member(
    val name: String,
    val email: String,
    val nickname: String,
    val birth: Date,
    val reg_date: Date,
    val photos: MutableList<Photo>,
    val tags: MutableList<Tag>,
    val regions: MutableList<Region>
)
