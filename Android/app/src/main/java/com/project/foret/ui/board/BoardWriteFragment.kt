package com.project.foret.ui.board

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.project.foret.MainActivity
import com.project.foret.R
import com.project.foret.model.Board
import com.project.foret.model.Foret
import com.project.foret.model.Member
import com.project.foret.repository.ForetRepository
import com.project.foret.util.Resource


class BoardWriteFragment : Fragment(R.layout.fragment_board_write) {

    lateinit var viewModel: BoardViewModel

    lateinit var progressBar: ProgressBar
    lateinit var etContent: EditText
    lateinit var etSubject: EditText
    lateinit var tvWriter: TextView
    lateinit var btnUpload: Button
    lateinit var spBoardType: Spinner
    lateinit var layoutImages: LinearLayout
    lateinit var toolbar: Toolbar

    val TAG = "BoardWriteFragment"

    var isAnonymous = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val foretRepository = ForetRepository()
        val viewModelProviderFactory = BoardViewModelProviderFactory(foretRepository)
        viewModel = ViewModelProvider(
            this,
            viewModelProviderFactory
        ).get(BoardViewModel::class.java)

        // layout
        progressBar = view.findViewById(R.id.progressBar)
        etContent = view.findViewById(R.id.etContent)
        etSubject = view.findViewById(R.id.etSubject)
        tvWriter = view.findViewById(R.id.tvWriter)
        btnUpload = view.findViewById(R.id.btnUpload)
        spBoardType = view.findViewById(R.id.spBoardType)
        layoutImages = view.findViewById(R.id.layoutImages)
        toolbar = view.findViewById(R.id.cosutom_toolbar)

        isAnonymous = arguments?.getBoolean("isAnonymous")!!

        if (isAnonymous) {
            setAnonymousBoardWrite()
        } else {
            setForetBoardWrite()
        }

        setCreateBoardResponse(view)

        toolbar.findViewById<TextView>(R.id.item_complete).setOnClickListener {
            createBoard()
        }
        Log.e(TAG, "onViewCreated: ${arguments?.getLong("foretId")}")
    }

    private fun createBoard() {
        val memberId = (activity as MainActivity).member_id
        val member = Member(memberId)
        val subject = etSubject.text.toString()
        val content = etContent.text.toString()
        val board: Board
        if (isAnonymous) {
            board = Board(4, subject, content, member, null)
        } else {
            var type = 0
            val foret = Foret(arguments?.getLong("foretId")!!)
            when (spBoardType.selectedItem) {
                "선택" -> {
                    Snackbar.make(spBoardType, "게시글 타입을 선택해주세요!", Snackbar.LENGTH_LONG).show()
                    return
                }
                "공지" -> {
                    type = 1
                }
                "일반" -> {
                    type = 3
                }
            }
            board = Board(type, subject, content, member, foret)
        }
        viewModel.createBoard(board)
    }

    private fun setCreateBoardResponse(view: View) {
        viewModel.createBoard.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { createResponse ->
                        val bundle = bundleOf("boardId" to createResponse.id.toLong())
                        view.findNavController()
                            .navigate(
                                R.id.action_boardWriteFragment_to_foretBoardFragment,
                                bundle
                            )
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

    private fun setAnonymousBoardWrite() {
        tvWriter.text = ""
        ArrayAdapter.createFromResource(
            activity as MainActivity,
            R.array.spAnonymousBoard,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spBoardType.adapter = adapter
            spBoardType.isEnabled = false
        }
        btnUpload.visibility = View.GONE
        layoutImages.visibility = View.GONE
    }

    private fun setForetBoardWrite() {
        tvWriter.text = (activity as MainActivity).member_name
        ArrayAdapter.createFromResource(
            activity as MainActivity,
            R.array.spForetBoard,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spBoardType.adapter = adapter
        }
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

}