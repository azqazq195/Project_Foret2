package com.project.foret.ui.foret

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.project.foret.R
import com.project.foret.adapter.BoardImageAdapter
import com.project.foret.adapter.CommentAdapter
import com.project.foret.model.Board
import com.project.foret.repository.ForetRepository
import com.project.foret.util.Constants.Companion.BASE_URL
import com.project.foret.util.Resource
import com.project.foret.util.ZoomOutPageTransformer

class ForetBoardFragment : Fragment(R.layout.fragment_foret_board) {

    lateinit var viewModel: ForetViewModel
    lateinit var boardImageAdapter: BoardImageAdapter
    lateinit var commentAdapter: CommentAdapter

    lateinit var progressBar: ProgressBar
    lateinit var tvForetBoardSubject: TextView
    lateinit var tvForetBoardContent: TextView
    lateinit var tvForetBoardComment: TextView
    lateinit var tvBoardReg_date: TextView
    lateinit var tvMemberName: TextView
    lateinit var ivProfileImage: ImageView
    lateinit var vpBoardImages: ViewPager2
    lateinit var rvComment: RecyclerView

    private val TAG = "ForetBoardFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // initialize
        // ViewModel, Repository
        val foretRepository = ForetRepository()
        val viewModelProviderFactory = ForetViewModelProviderFactory(foretRepository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(ForetViewModel::class.java)

        val id = arguments?.getLong("boardId")
        id?.let { viewModel.getBoardDetails(it) }

        // layout
        progressBar = view.findViewById(R.id.progressBar)
        tvForetBoardSubject = view.findViewById(R.id.tvForetBoardSubject)
        tvForetBoardContent = view.findViewById(R.id.tvForetBoardContent)
        tvForetBoardComment = view.findViewById(R.id.tvForetBoardComment)
        tvBoardReg_date = view.findViewById(R.id.tvBoardReg_date)
        tvMemberName = view.findViewById(R.id.tvMemberName)
        ivProfileImage = view.findViewById(R.id.ivProfileImage)
        vpBoardImages = view.findViewById(R.id.vpBoardImages)
        rvComment = view.findViewById(R.id.rvComment)

        setUpRecyclerView()
        setBoardData()
    }

    private fun setBoardData() {
        viewModel.foretBoard.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { boardResponse ->
                        setForetBoardView(boardResponse)
                        boardImageAdapter.differ.submitList(boardResponse.photos)
                        commentAdapter.differ.submitList(boardResponse.comments)
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

    private fun setUpRecyclerView() {
        boardImageAdapter = BoardImageAdapter()
        commentAdapter = CommentAdapter()
        vpBoardImages.apply {
            adapter = boardImageAdapter
            setPageTransformer(ZoomOutPageTransformer())
        }
        rvComment.apply {
            adapter = commentAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun setForetBoardView(board: Board) {
        tvForetBoardSubject.text = board.subject
        tvForetBoardContent.text = board.content
        tvBoardReg_date.text = board.reg_date.substring(0, board.reg_date.indexOf("T"))
        tvMemberName.text = board.member.name
        if(board.comments != null){
            tvForetBoardComment.text = "댓글 (${board.comments.size.toString()})"
            tvForetBoardComment.setOnClickListener{
//                Toast.makeText(this, "${viewModel.foretBoar}", Toast.LENGTH_SHORT).show()
            }
        }

        if(board.member.photos != null){
            Glide.with(this)
                .load("${BASE_URL}${board.member.photos[0].dir}/${board.member.photos[0].filename}")
                .error(R.drawable.home_icon_null_image)
                .thumbnail(0.1f)
                .into(ivProfileImage)
        } else {
            Glide.with(this)
                .load(R.drawable.home_icon_null_image)
                .thumbnail(0.1f)
                .into(ivProfileImage)
        }
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }
}