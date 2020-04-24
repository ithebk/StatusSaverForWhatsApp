package com.ithebk.statussaver.ui.main

import android.os.Bundle
import android.os.Debug
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.ithebk.statussaver.BuildConfig
import com.ithebk.statussaver.R
import com.ithebk.statussaver.data.Status
import com.ithebk.statussaver.ui.status.SavedStatusFragment
import com.ithebk.statussaver.ui.status.StatusFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        ).get(MainViewModel::class.java)
        if (supportActionBar != null) {
            supportActionBar?.elevation = 0f
        }

        init(savedInstanceState)
        MobileAds.initialize(this) {}
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    override fun onResume() {
        super.onResume()
//        if(mInterstitialAd.isLoaded){
//            mInterstitialAd.show()
//        }
    }

    private fun init(savedInstanceState: Bundle?) {
        mainViewModel.init()
        mainViewModel.statusList.observe(this, Observer { statusList ->
            setBottomNavigation(savedInstanceState, statusList)
        })
    }


    private fun setBottomNavigation(
        savedInstanceState: Bundle?,
        statusList: List<Status>
    ) {
        val statusListStr = Gson().toJson(statusList)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        if (savedInstanceState == null) {
            val fragment = StatusFragment.newInstance(statusListStr, R.id.navigation_images)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                .commit()
        }
        navView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_images, R.id.navigation_videos -> {
                    val fragment = StatusFragment.newInstance(statusListStr, menuItem.itemId)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_saved -> {
                    val fragment = SavedStatusFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                        .commit()
                    return@OnNavigationItemSelectedListener true

                }
            }
            false
        })

    }
}
