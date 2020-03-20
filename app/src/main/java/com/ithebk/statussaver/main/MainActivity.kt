package com.ithebk.statussaver.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavArgument
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ithebk.statussaver.R


class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setBottomNavigation()

        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        ).get(MainViewModel::class.java)
        mainViewModel.init()
        mainViewModel.imageList.observe(this, Observer {
            it.iterator().forEach {
                println(it.path)
            }
        })
    }

    private fun setBottomNavigation() {
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        val navInflater = navController.navInflater
        val graph = navInflater.inflate(R.navigation.mobile_navigation)

        val navArgument1= NavArgument.Builder().setDefaultValue(1).build()
        val navArgument2= NavArgument.Builder().setDefaultValue("Hello").build()
        graph.addArgument("Key1",navArgument1)
        graph.addArgument("Key2",navArgument2)
        navController.graph=graph

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

}
