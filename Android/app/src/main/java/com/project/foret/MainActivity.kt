package com.project.foret

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var foretNavHostFragment: Fragment

    val member_id = 5L
    val member_name = "문성하"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        foretNavHostFragment = supportFragmentManager.findFragmentById(R.id.foretNavHostFragment) as NavHostFragment
        bottomNavigationView.setupWithNavController(foretNavHostFragment.findNavController())
    }
}