package com.project.foret.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.foret.repository.ForetRepository
import com.project.foret.response.BoardResponse
import com.project.foret.response.ForetResponse
import com.project.foret.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class HomeViewModel(
    val foretRepository: ForetRepository
) : ViewModel() {

    val forets: MutableLiveData<Resource<ForetResponse>> = MutableLiveData()
    val foretBoards: MutableLiveData<Resource<BoardResponse>> = MutableLiveData()

    init {
        getMyForets(92L)
    }

    fun getMyForets(member_id: Long) = viewModelScope.launch {
        forets.postValue(Resource.Loading())
        val response = foretRepository.getMyForets(member_id)
        forets.postValue(handleForetResponse(response))
    }

    private fun handleForetResponse(response: Response<ForetResponse>) : Resource<ForetResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun getForetBoard(foret_id: Long) = viewModelScope.launch {
        foretBoards.postValue(Resource.Loading())
        val response = foretRepository.getForetBoard(foret_id)
        foretBoards.postValue(handleBoardResponse(response))
    }

    private fun handleBoardResponse(response: Response<BoardResponse>) : Resource<BoardResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}