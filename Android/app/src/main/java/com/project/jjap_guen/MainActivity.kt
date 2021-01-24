package com.project.jjap_guen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.jjap_guen.repository.ForetRepository
import com.project.jjap_guen.ui.home.HomeViewModel
import com.project.jjap_guen.ui.home.HomeViewModelProviderFactory

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var foretNavHostFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        foretNavHostFragment = supportFragmentManager.findFragmentById(R.id.foretNavHostFragment) as NavHostFragment
        bottomNavigationView.setupWithNavController(foretNavHostFragment.findNavController())
    }
}