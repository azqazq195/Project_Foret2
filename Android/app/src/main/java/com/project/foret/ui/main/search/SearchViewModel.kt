package com.project.foret.ui.main.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.foret.repository.ForetRepository
import com.project.foret.response.ForetResponse
import com.project.foret.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class SearchViewModel(
    val foretRepository: ForetRepository
) : ViewModel() {
    val forets: MutableLiveData<Resource<ForetResponse>> = MutableLiveData()

    fun getForetsByPage(page: Int, size: Int) = viewModelScope.launch {
        forets.postValue(Resource.Loading())
        val response = foretRepository.getForetsByPage(page, size)
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
}