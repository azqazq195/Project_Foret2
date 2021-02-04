package com.project.foret.ui.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.project.foret.MainActivity
import com.project.foret.R
import com.project.foret.adapter.BoardImageAdapter
import com.project.foret.adapter.CommentAdapter
import com.project.foret.repository.ForetRepository
import com.project.foret.ui.board.BoardViewModel
import com.project.foret.ui.board.BoardViewModelProviderFactory
import com.project.foret.ui.foret.CreateForetActivity
import com.project.foret.util.snackbar

class SearchFragment : Fragment(R.layout.fragment_search) {

    lateinit var viewModel: SearchViewModel

    lateinit var ivCreateForet: ImageView

    val TAG = "SearchFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // initialize
        // ViewModel, Repository
        val foretRepository = ForetRepository()
        val viewModelProviderFactory = SearchViewModelProviderFactory(foretRepository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(SearchViewModel::class.java)

        ivCreateForet = view.findViewById(R.id.ivCreateForet)

        ivCreateForet.setOnClickListener{
            val intent = Intent(context, CreateForetActivity::class.java)
            startActivityForResult(intent, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0){
            when(resultCode){
                AppCompatActivity.RESULT_OK -> {
                    val bundle = bundleOf("foretId" to data?.getLongExtra("foretId", 0L))
                    view?.findNavController()
                        ?.navigate(
                            R.id.action_searchFragment_to_foretFragment,
                            bundle
                        )
                }
                AppCompatActivity.RESULT_CANCELED -> {
                    (activity as MainActivity).mainLayoutRoot.snackbar("포레 생성 취소!")
                }
            }
        }
    }
}