package com.project.foret.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.project.foret.MainActivity
import com.project.foret.R
import com.project.foret.adapter.AnonymousAdapter
import com.project.foret.model.WriteBoard
import com.project.foret.repository.ForetRepository
import com.project.foret.ui.annoymousForum.AnonymousForumViewModel
import com.project.foret.ui.annoymousForum.AnonymousForumViewModelProviderFactory
import com.project.foret.util.Resource


class BoardWriteFragment : Fragment(R.layout.fragment_board_write) {

    lateinit var viewModel: AnonymousForumViewModel
    lateinit var anonymousAdapter: AnonymousAdapter

    lateinit var progressBar: ProgressBar
    lateinit var etContent: EditText
    lateinit var etSubject: EditText
    lateinit var tvWriter: TextView
    lateinit var btnUpload: Button
    lateinit var spBoardType: Spinner
    lateinit var layoutImages: LinearLayout
    lateinit var toolbar: Toolbar

    val TAG = "BoardWriteFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val foretRepository = ForetRepository()
        val viewModelProviderFactory = AnonymousForumViewModelProviderFactory(foretRepository)
        viewModel = ViewModelProvider(
            this,
            viewModelProviderFactory
        ).get(AnonymousForumViewModel::class.java)

        // layout
        progressBar = view.findViewById(R.id.progressBar)
        etContent = view.findViewById(R.id.etContent)
        etSubject = view.findViewById(R.id.etSubject)
        tvWriter = view.findViewById(R.id.tvWriter)
        btnUpload = view.findViewById(R.id.btnUpload)
        spBoardType = view.findViewById(R.id.spBoardType)
        layoutImages = view.findViewById(R.id.layoutImages)
        toolbar = view.findViewById(R.id.cosutom_toolbar)

        setDefault()
        setCreateBoardResponse(view)

        toolbar.findViewById<TextView>(R.id.item_complete).setOnClickListener {
            createBoard()
        }
    }

    private fun createBoard() {
        val member_id = (activity as MainActivity).member_id
        val board = WriteBoard(4, "제목1", "내용1")
        viewModel.createBoard(member_id, board)
    }

    private fun setCreateBoardResponse(view: View) {
        viewModel.createBoard.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { createResponse ->
                        val bundle = bundleOf("boardId" to createResponse.id)
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

    private fun setDefault() {
        tvWriter.text = ""
        ArrayAdapter.createFromResource(
            activity as MainActivity,
            R.array.spBoard,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spBoardType.adapter = adapter
            spBoardType.isEnabled = false
        }
        btnUpload.visibility = View.GONE
        layoutImages.visibility = View.GONE

    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

}