package com.project.foret.ui.AnnoymousForum

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.project.foret.R
import com.project.foret.adapter.AnonymousAdapter
import com.project.foret.adapter.BoardItemAdapter
import com.project.foret.adapter.ForetAdapter
import com.project.foret.model.Board
import com.project.foret.repository.ForetRepository
import com.project.foret.ui.home.HomeViewModel
import com.project.foret.ui.home.HomeViewModelProviderFactory
import com.project.foret.util.Resource
import com.project.foret.util.ZoomOutPageTransformer

class AnonymousForumFragment : Fragment(R.layout.fragment_anonymousforum) {

    lateinit var viewModel: AnonymousForumViewModel
    lateinit var anonymousAdapter: AnonymousAdapter

    lateinit var rvAnonyBoard: RecyclerView
    lateinit var tvRecent: TextView
    lateinit var tvLikeRank: TextView
    lateinit var tvCommentRank: TextView
    lateinit var progressBar: ProgressBar
    lateinit var btnAnonyBoardWrite: FloatingActionButton

    private val TAG = "AnonymousForumFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // initialize
        // ViewModel, Repository
        val foretRepository = ForetRepository()
        val viewModelProviderFactory = AnonymousForumViewModelProviderFactory(foretRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(AnonymousForumViewModel::class.java)

        // layout
        progressBar = view.findViewById(R.id.progressBar)
        rvAnonyBoard = view.findViewById(R.id.rvAnonyBoard)
        tvRecent = view.findViewById(R.id.tvRecent)
        tvLikeRank = view.findViewById(R.id.tvLikeRank)
        tvCommentRank = view.findViewById(R.id.tvCommentRank)
        btnAnonyBoardWrite = view.findViewById(R.id.btnAnonyBoardWrite)

        viewModel.getAnonymousBoardList()

        setUpRecyclerView()
        setBoardData()

        anonymousAdapter.setOnItemClickListener {
            val bundle = bundleOf("boardId" to it.id)
            view.findNavController()
                .navigate(
                    R.id.action_anonymousForumFragment_to_foretBoardFragment,
                    bundle
                )
        }

        btnAnonyBoardWrite.setOnClickListener{
        }
    }

    private fun setUpRecyclerView() {
        anonymousAdapter = AnonymousAdapter()

        rvAnonyBoard.apply {
            adapter = anonymousAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun setBoardData() {
        viewModel.anonymousBoardList.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { boardResponse ->
                        anonymousAdapter.differ.submitList(boardResponse.boards)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "An error occured $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

}