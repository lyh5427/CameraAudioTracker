package com.yunho.king.presentation.ui.main

import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.Bundle
import android.provider.Settings.Global
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.LoadAdError
import com.google.android.material.navigation.NavigationBarView
import com.google.common.util.concurrent.ListenableFuture
import com.yunho.king.GlobalApplication
import com.yunho.king.R
import com.yunho.king.Utils.toVisible
import com.yunho.king.databinding.ActivityMainBinding
import com.yunho.king.databinding.PopupSuspicionBinding
import com.yunho.king.presentation.service.MainService
import com.yunho.king.presentation.ui.base.BaseActivity
import com.yunho.king.presentation.ui.main.fragment.ExceptFragment
import com.yunho.king.presentation.ui.main.fragment.HoleFragment
import com.yunho.king.presentation.ui.main.fragment.usage.UsageFragment

class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater, null, false)

        setContentView(binding.root)

        setBottomNavi()

        setAdmobview()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(Intent(this, MainService::class.java))
        } else {
            startService(Intent(this, MainService::class.java))
        }

    }

    private fun setAdmobview() = with(binding) {
        val adRequest = AdRequest.Builder().build()
        admobView.loadAd(adRequest)
        admobView.adListener = object : AdListener() {
            override fun onAdFailedToLoad(error: LoadAdError) {
                Log.e(GlobalApplication.TagName, error.message)
            }
        }
    }

    private fun setBottomNavi() = with(binding) {
        bottomNavi.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener {
            when (it.itemId) {
                R.id.usage -> {
                    moveToUsage()
                    return@OnItemSelectedListener true
                }

                R.id.except -> {
                    moveToExcept()
                    return@OnItemSelectedListener true
                }

                R.id.hole -> {
                    moveToHole()
                    return@OnItemSelectedListener true
                }

                else -> return@OnItemSelectedListener false
            }
        })

        bottomNavi.selectedItemId = R.id.usage
    }

    private fun moveToUsage() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainFragmentContainer, UsageFragment())
            .commit()
    }

    private fun moveToExcept() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainFragmentContainer, ExceptFragment())
        fragmentTransaction.commit()
    }

    private fun moveToHole() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainFragmentContainer, HoleFragment())
        fragmentTransaction.commit()
    }
}