package com.project.foret.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.foret.R

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var foretNavHostFragment: Fragment
    lateinit var mainLayoutRoot: ConstraintLayout

    val member_id = 5L
    val member_name = "문성하"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainLayoutRoot = findViewById(R.id.mainLayoutRoot)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        foretNavHostFragment = supportFragmentManager.findFragmentById(R.id.foretNavHostFragment) as NavHostFragment
        bottomNavigationView.setupWithNavController(foretNavHostFragment.findNavController())
    }
}