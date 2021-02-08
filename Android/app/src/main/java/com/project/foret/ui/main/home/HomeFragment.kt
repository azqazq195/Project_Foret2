package com.project.foret.ui.main.home

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.project.foret.ui.main.MainActivity
import com.project.foret.R
import com.project.foret.adapter.BoardAdapter
import com.project.foret.adapter.ForetThumAdapter
import com.project.foret.model.Board
import com.project.foret.repository.ForetRepository
import com.project.foret.util.Resource
import com.project.foret.util.ZoomOutPageTransformer

class HomeFragment : Fragment(R.layout.fragment_home) {

    lateinit var viewModel: HomeViewModel
    lateinit var foretThumAdapter: ForetThumAdapter
    lateinit var noticeAdapter: BoardAdapter
    lateinit var feedAdapter: BoardAdapter

    lateinit var vpForetImages: ViewPager2
    lateinit var rvNotice: RecyclerView
    lateinit var rvFeed: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var tvForetName: TextView
    lateinit var tvMoreForets: TextView
    lateinit var cvNoForet: CardView

    private val TAG = "HomFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // initialize
        // ViewModel, Repository
        val foretRepository = ForetRepository()
        val viewModelProviderFactory = HomeViewModelProviderFactory(foretRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(HomeViewModel::class.java)

        setFindViewById(view)

        (activity as MainActivity).viewModel.member.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    viewModel.getMyForets((activity as MainActivity).member?.id!!)
                }
                is Resource.Error -> {
                    hideProgressBar()
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

        setToolbar()
        setUpRecyclerView()
        setViewPagerChangeListener()
        setClickListener()

        setForetData()
        setBoardData()
    }

    private fun setFindViewById(view: View) {
        progressBar = view.findViewById(R.id.progressBar)
        vpForetImages = view.findViewById(R.id.vpForetImages)
        rvNotice = view.findViewById(R.id.rvForetTag)
        rvFeed = view.findViewById(R.id.rvFeed)
        tvForetName = view.findViewById(R.id.tvForetName)
        tvMoreForets = view.findViewById(R.id.tvMoreForets)
        cvNoForet = view.findViewById(R.id.cvNoForet)
    }

    private fun setToolbar() {
        setHasOptionsMenu(true)
        val mContext = (activity as MainActivity)
        mContext.toolbar.setBackgroundColor(
            ContextCompat.getColor(mContext, R.color.textForet)
        )
        mContext.ivToolbar.setImageDrawable(
            ContextCompat.getDrawable(mContext, R.drawable.foret_logo_white)
        )
    }

    private fun showEmpty() {
        cvNoForet.visibility = View.VISIBLE
        vpForetImages.visibility = View.INVISIBLE
        tvForetName.text = "가입한 포레가 없습니다!"
    }

    private fun showForets() {
        cvNoForet.visibility = View.INVISIBLE
        vpForetImages.visibility = View.VISIBLE
    }

    private fun setUpRecyclerView() {
        foretThumAdapter = ForetThumAdapter()
        noticeAdapter = BoardAdapter()
        feedAdapter = BoardAdapter()

        vpForetImages.apply {
            adapter = foretThumAdapter
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

    private fun setClickListener() {
        foretThumAdapter.setOnItemClickListener {
            val bundle = bundleOf("foretId" to it.id)
            view?.findNavController()
                ?.navigate(
                    R.id.action_homeFragment_to_foretFragment,
                    bundle
                )
        }
        noticeAdapter.setOnItemClickListener {
            val bundle = bundleOf("boardId" to it.id)
            view?.findNavController()
                ?.navigate(
                    R.id.action_homeFragment_to_foretBoardFragment,
                    bundle
                )
        }
        feedAdapter.setOnItemClickListener {
            val bundle = bundleOf("boardId" to it.id)
            view?.findNavController()
                ?.navigate(
                    R.id.action_homeFragment_to_foretBoardFragment,
                    bundle
                )
        }
        tvMoreForets.setOnClickListener {
            view?.findNavController()
                ?.navigate(
                    R.id.action_homeFragment_to_searchFragment,
                )
        }
    }

    private fun setForetData() {
        viewModel.forets.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { foretResponse ->
                        if (foretResponse.total == 0) {
                            showEmpty()
                        } else {
                            foretThumAdapter.differ.submitList(foretResponse.forets)
                            showForets()
                        }
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

    private fun setBoardData() {
        viewModel.homeBoardList.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { boardResponse ->
                        val noticeBoard: MutableList<Board> = mutableListOf()
                        val feedBoard: MutableList<Board> = mutableListOf()
                        if (boardResponse.total != 0) {
                            for (i in boardResponse.boards) {
                                if (i.type == 1) {
                                    noticeBoard.add(i)
                                } else if (i.type == 3) {
                                    feedBoard.add(i)
                                }
                            }
                        }
                        noticeAdapter.differ.submitList(noticeBoard)
                        feedAdapter.differ.submitList(feedBoard)
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

    private fun setViewPagerChangeListener() {
        vpForetImages.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tvForetName.text = foretThumAdapter.differ.currentList[position].name
                viewModel.getHomeBoardList(foretThumAdapter.differ.currentList[position].id!!)
            }
        })
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_home, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.drawer_menu) {
            (activity as MainActivity).drawerLayout.openDrawer(GravityCompat.END)
        }
        return super.onOptionsItemSelected(item)
    }
}