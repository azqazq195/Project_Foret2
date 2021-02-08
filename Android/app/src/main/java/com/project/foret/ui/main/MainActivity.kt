package com.project.foret.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.project.foret.R
import com.project.foret.ui.entrance.signIn.SignInActivity
import com.project.foret.ui.entrance.signUp.SignUpInfoActivity
import com.project.foret.util.snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var drawerNavigationView: NavigationView
    private lateinit var foretNavHostFragment: Fragment

    lateinit var drawerLayout: DrawerLayout
    lateinit var mainLayoutRoot: ConstraintLayout

    lateinit var toolbar: Toolbar
    lateinit var ivToolbar: ImageView

    private lateinit var ivDrawerCancel: ImageView
    private lateinit var tvLogout: TextView

    var pressedTime: Long = 0

    val member_id = 5L
    val member_name = "문성하"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setFindViewById()
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        bottomNavigationView.setupWithNavController(foretNavHostFragment.findNavController())
        setOnClickListener()
    }

    private fun setFindViewById() {
        drawerLayout = findViewById(R.id.drawerLayout)
        mainLayoutRoot = findViewById(R.id.mainLayoutRoot)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        drawerNavigationView = findViewById(R.id.drawerNavigationView)
        foretNavHostFragment =
            supportFragmentManager.findFragmentById(R.id.foretNavHostFragment) as NavHostFragment
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
        ivDrawerCancel.setOnClickListener{
            drawerLayout.closeDrawer(GravityCompat.END)
        }
        tvLogout.setOnClickListener { 
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}