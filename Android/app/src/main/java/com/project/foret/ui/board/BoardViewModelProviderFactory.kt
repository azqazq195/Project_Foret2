package com.project.foret.ui.board

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.foret.repository.ForetRepository
import com.project.foret.ui.foret.ForetViewModel

class BoardViewModelProviderFactory(
    private val foretRepository: ForetRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BoardViewModel(foretRepository) as T
    }
}