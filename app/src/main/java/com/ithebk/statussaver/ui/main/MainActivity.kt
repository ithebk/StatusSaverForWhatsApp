package com.ithebk.statussaver.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.ithebk.statussaver.R
import com.ithebk.statussaver.data.Status
import com.ithebk.statussaver.ui.status.StatusFragment


class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        ).get(MainViewModel::class.java)
        mainViewModel.init()
        mainViewModel.imageList.observe(this, Observer {
            setBottomNavigation(savedInstanceState, it)
        })


    }

    private fun setBottomNavigation(
        savedInstanceState: Bundle?,
        statusList: List<Status>) {
        val statusListStr = Gson().toJson(statusList)
        println("Printing Time")
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        if (savedInstanceState == null) {
            val fragment =  StatusFragment.newInstance(statusListStr, R.id.navigation_images)
            supportFragmentManager.beginTransaction().replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                .commit()
        }
        navView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_images, R.id.navigation_videos, R.id.navigation_saved -> {
                    val fragment = StatusFragment.newInstance(statusListStr,menuItem.itemId)
                    supportFragmentManager.beginTransaction().replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })

    }
}
