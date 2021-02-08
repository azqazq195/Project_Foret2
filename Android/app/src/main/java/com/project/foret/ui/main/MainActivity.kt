package com.project.foret.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.project.foret.R
import com.project.foret.model.Member
import com.project.foret.repository.ForetRepository
import com.project.foret.ui.entrance.signIn.SignInActivity
import com.project.foret.ui.entrance.signIn.SignInViewModel
import com.project.foret.ui.entrance.signIn.SignInViewModelProviderFactory
import com.project.foret.ui.entrance.signUp.SignUpInfoActivity
import com.project.foret.util.Resource
import com.project.foret.util.snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var drawerNavigationView: NavigationView
    private lateinit var foretNavHostFragment: Fragment
    lateinit var viewModel: MainViewModel
    private lateinit var progressBar: ProgressBar

    lateinit var drawerLayout: DrawerLayout
    lateinit var mainLayoutRoot: ConstraintLayout

    lateinit var toolbar: Toolbar
    lateinit var ivToolbar: ImageView

    private lateinit var ivDrawerCancel: ImageView
    private lateinit var tvLogout: TextView

    var pressedTime: Long = 0

    var member: Member? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val foretRepository = ForetRepository()
        val viewModelProviderFactory = MainViewModelProviderFactory(foretRepository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(MainViewModel::class.java)

        setFindViewById()

        viewModel.getMember(intent.getLongExtra("id", 0L))

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        foretNavHostFragment =
            supportFragmentManager.findFragmentById(R.id.foretNavHostFragment) as NavHostFragment
        bottomNavigationView.setupWithNavController(foretNavHostFragment.findNavController())
        setOnClickListener()
        setMember()
    }

    private fun setMember() {
        viewModel.member.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    member = response.data!!
                    member?.id = intent.getLongExtra("id", 0L)
                }
                is Resource.Error -> {
                    hideProgressBar()
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun setFindViewById() {
        progressBar = findViewById(R.id.progressBar)
        drawerLayout = findViewById(R.id.drawerLayout)
        mainLayoutRoot = findViewById(R.id.mainLayoutRoot)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        drawerNavigationView = findViewById(R.id.drawerNavigationView)
        toolbar = findViewById(R.id.toolbar)
        ivToolbar = findViewById(R.id.ivToolbar)

        ivDrawerCancel = findViewById(R.id.ivDrawerCancel)
        tvLogout = findViewById(R.id.tvLogout)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END)
            return
        }

        if (pressedTime == 0L) {
            Toast.makeText(this@MainActivity, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
            pressedTime = System.currentTimeMillis()
        } else {
            val seconds = (System.currentTimeMillis() - pressedTime) as Int
            if (seconds > 2000) {
                Toast.makeText(this@MainActivity, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
                pressedTime = 0
            } else {
                super.onBackPressed()
                finish()
            }
        }
    }

    private fun setOnClickListener() {
        ivDrawerCancel.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.END)
        }
        tvLogout.setOnClickListener {
            Log.e("TAG", "setOnClickListener: logout", )
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }
}