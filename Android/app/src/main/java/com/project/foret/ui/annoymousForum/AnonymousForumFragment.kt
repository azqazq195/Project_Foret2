package com.project.foret.ui.annoymousForum

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.project.foret.repository.ForetRepository
import com.project.foret.util.Resource

class AnonymousForumFragment : Fragment(R.layout.fragment_anonymousforum) {

    lateinit var viewModel: AnonymousForumViewModel
    lateinit var anonymousAdapter: AnonymousAdapter

    lateinit var rvAnonyBoard: RecyclerView
    lateinit var tvRecent: TextView
    lateinit var tvLikeRank: TextView
    lateinit var tvCommentRank: TextView
    lateinit var progressBar: ProgressBar
    lateinit var btnBoardWrite: FloatingActionButton

    private val TAG = "AnonymousForumFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // initialize
        // ViewModel, Repository
        val foretRepository = ForetRepository()
        val viewModelProviderFactory = AnonymousForumViewModelProviderFactory(foretRepository)
        viewModel = ViewModelProvider(
            this,
            viewModelProviderFactory
        ).get(AnonymousForumViewModel::class.java)

        // layout
        progressBar = view.findViewById(R.id.progressBar)
        rvAnonyBoard = view.findViewById(R.id.rvAnonyBoard)
        tvRecent = view.findViewById(R.id.tvRecent)
        tvLikeRank = view.findViewById(R.id.tvLikeRank)
        tvCommentRank = view.findViewById(R.id.tvCommentRank)
        btnBoardWrite = view.findViewById(R.id.btnBoardWrite)

        // 정렬
        orderBy(1)

        setUpRecyclerView()
        setBoardData()

        anonymousAdapter.setOnItemClickListener {
            view.findNavController()
                .navigate(
                    R.id.action_anonymousForumFragment_to_foretBoardFragment,
                    bundleOf(
                        "boardId" to it.id,
                        "isAnonymous" to true
                    )
                )
        }

        tvRecent.setOnClickListener { orderBy(1) }
        tvCommentRank.setOnClickListener { orderBy(2) }
        tvLikeRank.setOnClickListener { orderBy(3) }

        btnBoardWrite.setOnClickListener {
            view.findNavController()
                .navigate(
                    R.id.action_anonymousForumFragment_to_boardWriteFragment,
                    bundleOf(
                        "isAnonymous" to true
                    )
                )
        }
    }

    private fun orderBy(order: Int) {
        // viewModel.getAnonymousBoardList(order)
        when (order) {
            1 -> {
                viewModel.getAnonymousBoardList(1)
                tvRecent.setTextColor(Color.parseColor("#FF22997b"))
                tvRecent.setTypeface(tvRecent.typeface, Typeface.BOLD)
                tvCommentRank.setTextColor(Color.parseColor("#bbbbbb"))
                tvCommentRank.setTypeface(tvCommentRank.typeface, Typeface.NORMAL)
                tvLikeRank.setTextColor(Color.parseColor("#bbbbbb"))
                tvLikeRank.setTypeface(tvLikeRank.typeface, Typeface.NORMAL)
            }
            2 -> {
                viewModel.getAnonymousBoardList(2)
                tvRecent.setTextColor(Color.parseColor("#bbbbbb"))
                tvRecent.setTypeface(tvRecent.typeface, Typeface.NORMAL)
                tvCommentRank.setTextColor(Color.parseColor("#FF22997b"))
                tvCommentRank.setTypeface(tvCommentRank.typeface, Typeface.BOLD)
                tvLikeRank.setTextColor(Color.parseColor("#bbbbbb"))
                tvLikeRank.setTypeface(tvLikeRank.typeface, Typeface.NORMAL)
            }
            3 -> {
                viewModel.getAnonymousBoardList(1)
                tvRecent.setTextColor(Color.parseColor("#bbbbbb"))
                tvRecent.setTypeface(tvRecent.typeface, Typeface.NORMAL)
                tvCommentRank.setTextColor(Color.parseColor("#bbbbbb"))
                tvCommentRank.setTypeface(tvCommentRank.typeface, Typeface.NORMAL)
                tvLikeRank.setTextColor(Color.parseColor("#FF22997b"))
                tvLikeRank.setTypeface(tvLikeRank.typeface, Typeface.BOLD)
            }
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