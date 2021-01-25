package com.project.foret.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.project.foret.R
import com.project.foret.adapter.BoardAdapter
import com.project.foret.adapter.ForetAdapter
import com.project.foret.model.Board
import com.project.foret.repository.ForetRepository
import com.project.foret.util.Resource
import com.project.foret.util.ZoomOutPageTransformer

class HomeFragment : Fragment(R.layout.fragment_home) {

    lateinit var viewModel: HomeViewModel
    lateinit var foretAdapter: ForetAdapter
    lateinit var noticeAdapter: BoardAdapter
    lateinit var feedAdapter: BoardAdapter

    lateinit var vpForetImages: ViewPager2
    lateinit var rvNotice: RecyclerView
    lateinit var rvFeed: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var tvForetName: TextView

    private val TAG = "HomFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // initialize
        // ViewModel, Repository
        val foretRepository = ForetRepository()
        val viewModelProviderFactory = HomeViewModelProviderFactory(foretRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(HomeViewModel::class.java)

        // layout
        progressBar = view.findViewById(R.id.progressBar)
        vpForetImages = view.findViewById(R.id.vpForetImages)
        rvNotice = view.findViewById(R.id.rvNotice)
        rvFeed = view.findViewById(R.id.rvFeed)
        tvForetName = view.findViewById(R.id.tvForetName)

        setUpRecyclerView()
        setForetData()
        setBoardData()
        setViewPagerChangeListener()
    }

    private fun setUpRecyclerView() {
        foretAdapter = ForetAdapter()
        noticeAdapter = BoardAdapter()
        feedAdapter = BoardAdapter()

        vpForetImages.apply {
            adapter = foretAdapter
            setPageTransformer(ZoomOutPageTransformer())
        }
        rvNotice.apply {
            adapter = noticeAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        rvFeed.apply {
            adapter = feedAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun setForetData() {
        viewModel.forets.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { foretResponse ->
                        foretAdapter.differ.submitList(foretResponse.forets)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "An error occured $message" )
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun setBoardData() {
        viewModel.foretBoards.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { boardResponse ->
                        if(boardResponse.total != 0) {
                            val noticeBoard: MutableList<Board> = mutableListOf()
                            val feedBoard: MutableList<Board> = mutableListOf()
                            for(i in boardResponse.boards){
                                if(i.type == 1){
                                    noticeBoard.add(i)
                                } else if (i.type == 3){
                                    feedBoard.add(i)
                                }
                            }
                            noticeAdapter.differ.submitList(noticeBoard)
                            feedAdapter.differ.submitList(feedBoard)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "An error occured $message" )
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun setViewPagerChangeListener() {
        vpForetImages.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tvForetName.text = foretAdapter.differ.currentList[position].name
                viewModel.getForetBoard(foretAdapter.differ.currentList[position].id)
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