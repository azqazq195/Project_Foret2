package com.project.foret.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.foret.repository.ForetRepository
import com.project.foret.ui.board.BoardViewModel

class SearchViewModelProviderFactory(
    private val foretRepository: ForetRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchViewModel(foretRepository) as T
    }
}