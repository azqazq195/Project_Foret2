package com.project.foret.ui.AnnoymousForum

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.foret.model.Board
import com.project.foret.repository.ForetRepository
import com.project.foret.response.BoardListResponse
import com.project.foret.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class AnonymousForumViewModel(
    val foretRepository: ForetRepository
) : ViewModel() {

    val anonymousBoard: MutableLiveData<Resource<Board>> = MutableLiveData()
    val anonymousBoardList: MutableLiveData<Resource<BoardListResponse>> = MutableLiveData()

    fun getBoardDetails(board_id: Long) = viewModelScope.launch {
        anonymousBoard.postValue(Resource.Loading())
        val response = foretRepository.getBoardDetails(board_id)
        anonymousBoard.postValue(handleBoardDetailsResponse(response))
    }

    private fun handleBoardDetailsResponse(response: Response<Board>) : Resource<Board> {
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun getAnonymousBoardList() = viewModelScope.launch {
        anonymousBoardList.postValue(Resource.Loading())
        val response = foretRepository.getAnonymousBoardList()
        anonymousBoardList.postValue(handleAnonymousBoardListResponse(response))
    }

    private fun handleAnonymousBoardListResponse(response: Response<BoardListResponse>) : Resource<BoardListResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}