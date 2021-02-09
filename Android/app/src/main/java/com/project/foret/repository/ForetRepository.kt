package com.project.foret.repository

import com.project.foret.api.RetrofitInstance
import com.project.foret.model.Board
import com.project.foret.model.Comment
import com.project.foret.model.Foret
import com.project.foret.model.Member
import com.project.foret.response.CreateResponse
import com.project.foret.response.SignInResponse
import com.project.foret.response.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response

class ForetRepository {
    suspend fun getForets() =
        RetrofitInstance.api.getForets()

    suspend fun getForetsByPage(page: Int, size: Int) =
        RetrofitInstance.api.getForetsByPage(page, size)

    suspend fun getMyForets(member_id: Long) =
        RetrofitInstance.api.getMyForets(member_id)

    suspend fun getHomeBoardList(foret_id: Long) =
        RetrofitInstance.api.getHomeBoardList(foret_id)

    suspend fun getForetBoardList(foret_id: Long, type: Int) =
        RetrofitInstance.api.getForetBoardList(foret_id, type)

    suspend fun getForetDetails(foret_id: Long) =
        RetrofitInstance.api.getForetDetails(foret_id)

    suspend fun getBoardDetails(board_id: Long) =
        RetrofitInstance.api.getBoardDetails(board_id)

    suspend fun getAnonymousBoardList(order: Int) =
        RetrofitInstance.api.getAnonymousBoardList(order)

    suspend fun getComments(board_id: Long) =
        RetrofitInstance.api.getComments(board_id)

//    suspend fun createBoard(board: Board): Response<CreateResponse> =
//        RetrofitInstance.api.createBoard(board)

    suspend fun createComment(comment: Comment): Response<CreateResponse> =
        RetrofitInstance.api.createComment(comment)

    fun createForet(files: MultipartBody.Part, foret: RequestBody): Call<UploadResponse> =
        RetrofitInstance.api.createForet(files, foret)

    fun createBoard(files: List<MultipartBody.Part>?, board: RequestBody): Call<UploadResponse> =
        RetrofitInstance.api.createBoard(files, board)

    suspend fun createMember(files: MultipartBody.Part, member: RequestBody): Response<UploadResponse> =
        RetrofitInstance.api.createMember(files, member)

    suspend fun signIn(member: HashMap<String, String>): Response<SignInResponse> =
        RetrofitInstance.api.signIn(member)

    suspend fun getMember(member_id: Long) =
        RetrofitInstance.api.getMember(member_id)

}