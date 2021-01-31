package com.project.foret.ui.board

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.foret.model.Board
import com.project.foret.model.Comment
import com.project.foret.model.WriteBoard
import com.project.foret.repository.ForetRepository
import com.project.foret.response.CreateResponse
import com.project.foret.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class BoardViewModel(
    val foretRepository: ForetRepository
) : ViewModel() {

    val board: MutableLiveData<Resource<Board>> = MutableLiveData()
    val createBoard: MutableLiveData<Resource<CreateResponse>> = MutableLiveData()

    fun getBoardDetails(board_id: Long) = viewModelScope.launch {
        board.postValue(Resource.Loading())
        val response = foretRepository.getBoardDetails(board_id)
        board.postValue(handleBoardDetailsResponse(response))
    }

    private fun handleBoardDetailsResponse(response: Response<Board>) : Resource<Board> {
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun createBoard(member_id: Long, board: WriteBoard) = viewModelScope.launch {
        createBoard.postValue(Resource.Loading())
        val response = foretRepository.createBoard(member_id,board)
        createBoard.postValue(handleCreateBoardResponse(response))
    }

    private fun handleCreateBoardResponse(response: Response<CreateResponse>): Resource<CreateResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}