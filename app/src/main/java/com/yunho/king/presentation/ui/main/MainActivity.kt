package com.yunho.king.presentation.ui.main

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.google.android.material.navigation.NavigationBarView
import com.yunho.king.R
import com.yunho.king.databinding.ActivityMainBinding
import com.yunho.king.presentation.service.MainService
import com.yunho.king.presentation.ui.base.BaseActivity
import com.yunho.king.presentation.ui.main.fragment.ExceptFragment
import com.yunho.king.presentation.ui.main.fragment.HoleFragment
import com.yunho.king.presentation.ui.main.fragment.UsageFragment

class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setBottomNavi()
        startForegroundService(Intent(this, MainService::class.java))
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
    }

    private fun moveToUsage() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainFragmentContainer, UsageFragment())
        fragmentTransaction.commit()
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