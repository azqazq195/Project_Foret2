package com.project.foret.ui.foret

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.foret.model.Board
import com.project.foret.repository.ForetRepository
import com.project.foret.response.BoardListResponse
import com.project.foret.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class ForetViewModel(
    val foretRepository: ForetRepository
) : ViewModel() {

    val foretBoard: MutableLiveData<Resource<Board>> = MutableLiveData()

    fun getBoardDetails(board_id: Long) = viewModelScope.launch {
        foretBoard.postValue(Resource.Loading())
        val response = foretRepository.getBoardDetails(board_id)
        foretBoard.postValue(handleBoardDetailsResponse(response))
    }

    private fun handleBoardDetailsResponse(response: Response<Board>) : Resource<Board> {
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}