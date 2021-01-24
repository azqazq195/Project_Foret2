package com.project.jjap_guen.repository

import com.project.jjap_guen.api.RetrofitInstance

class ForetRepository {
    suspend fun getMyForets(member_id: Long) = RetrofitInstance.api.getMyForets(member_id)
    suspend fun getForetBoard(foret_id: Long) = RetrofitInstance.api.getForetBoard(foret_id)
}