package com.project.foret.ui.board

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.project.foret.MainActivity
import com.project.foret.R
import com.project.foret.adapter.BoardImageAdapter
import com.project.foret.adapter.CommentAdapter
import com.project.foret.model.Board
import com.project.foret.model.Comment
import com.project.foret.model.Member
import com.project.foret.repository.ForetRepository
import com.project.foret.util.Constants.Companion.BASE_URL
import com.project.foret.util.Resource
import com.project.foret.util.ZoomOutPageTransformer
import java.util.*


class BoardFragment : Fragment(R.layout.fragment_board) {

    lateinit var viewModel: BoardViewModel
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

    // 댓글
    lateinit var rvComment: RecyclerView
    lateinit var etComment: EditText
    lateinit var btnCommentWrite: Button
    lateinit var tvReCommentTarget: TextView
    lateinit var tvReCommentTargetCancel: TextView
    lateinit var layoutReComment: LinearLayout

    private val TAG = "BoardFragment"

    private var reCommentToId: Long? = null
    private var id: Long = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // initialize
        // ViewModel, Repository
        val foretRepository = ForetRepository()
        val viewModelProviderFactory = BoardViewModelProviderFactory(foretRepository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(BoardViewModel::class.java)

        id = arguments?.getLong("boardId")!!
        viewModel.getBoardDetails(id)
        viewModel.getComments(id)

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
        etComment = view.findViewById(R.id.etComment)
        btnCommentWrite = view.findViewById(R.id.btnCommentWrite)
        tvReCommentTarget = view.findViewById(R.id.tvReCommentTarget)
        tvReCommentTargetCancel = view.findViewById(R.id.tvReCommentTargetCancel)
        layoutReComment = view.findViewById(R.id.layoutReComment)

        setUpRecyclerView()
        setBoardData()
        setCommentData()
        setCommentClickListener()
    }

    private fun setBoardData() {
        viewModel.board.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { boardResponse ->
                        setBoardView(boardResponse)
                        boardImageAdapter.differ.submitList(boardResponse.photos)
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

    @SuppressLint("SetTextI18n")
    private fun setCommentData() {
        viewModel.commentList.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { commentResponse ->
                        tvForetBoardComment.text = "댓글 (${commentResponse.total})"
                        commentAdapter.differ.submitList(commentResponse.comments)
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

        viewModel.createComment.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { commentResponse ->
                        Snackbar.make(rvComment, "댓글 입력 성공", Snackbar.LENGTH_SHORT).show()
                        viewModel.getComments(id)
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

    private fun createComment() {
        val memberId = (activity as MainActivity).member_id
        val member = Member(memberId)
        val board = Board(arguments?.getLong("boardId")!!)
        val comment = Comment(reCommentToId, etComment.text.toString(), member, board)
        viewModel.createComment(comment)
        hideKeyboard()
        hideReComment()
        etComment.setText("")
    }

    private fun setCommentClickListener() {
        btnCommentWrite.setOnClickListener {
            if (etComment.text.toString().trim() == "") {
                Snackbar.make(rvComment, "내용을 입력해 주세요", Snackbar.LENGTH_SHORT).show()
            } else {
                createComment()
            }
        }

        tvReCommentTargetCancel.setOnClickListener {
            hideKeyboard()
            hideReComment()
        }

        commentAdapter.setOnClickListener(object : CommentAdapter.OnClickListener {
            override fun onReCommentClick(v: View, position: Int) {
                showReComment(position)
                showKeyboard()
            }

            @SuppressLint("SetTextI18n")
            override fun onEditCommentClick(v: View, position: Int) {
                Log.e(TAG, "onClick: $position 수정")
            }

            override fun onDeleteCommentClick(v: View, position: Int) {
                Log.e(TAG, "onClick: $position 삭제")
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

    @SuppressLint("SetTextI18n")
    private fun setBoardView(board: Board) {
        val isAnonymous: Boolean
        if (arguments?.getBoolean("isAnonymous") == null) {
            isAnonymous = false
        } else {
            isAnonymous = arguments?.getBoolean("isAnonymous")!!
        }

        if (isAnonymous) {
            // 작성자 닉네임
            tvMemberName.text = board.member?.nickname
            // 작성자 프로필 사진
            ivProfileImage.visibility = View.GONE
            vpBoardImages.visibility = View.GONE
        } else {
            // 작성자 이름
            tvMemberName.text = board.member?.name

            // 작성자 프로필 사진
            if (board.member?.photos != null) {
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

        // 작성일
        tvBoardReg_date.text = board.reg_date?.substring(0, board.reg_date.indexOf("T"))
        tvForetBoardSubject.text = board.subject
        tvForetBoardContent.text = board.content
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideKeyboard() {
        val imm =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun showKeyboard() {
        val imm =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(etComment, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideReComment() {
        if (reCommentToId != null) {
            reCommentToId = null
            val text = etComment.text
            val start = text.indexOf("@")
            val end = text.indexOf(" ")
            val newText = text.substring(text.substring(start, end).length + 1)
            etComment.setText(newText)
            layoutReComment.visibility = View.GONE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showReComment(position: Int) {
        reCommentToId = commentAdapter.differ.currentList[position].group_id
        val target = commentAdapter.differ.currentList[position].member?.nickname.toString()
        val text = etComment.text
        val mention = "@$target "
        tvReCommentTarget.text = "$target 님께 답글 작성중입니다..."
        etComment.setText(mention + text)
        etComment.setSelection(etComment.text.length)
        layoutReComment.visibility = View.VISIBLE
    }
}