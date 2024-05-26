package com.yunho.king.presentation.ui.main

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.yunho.king.R
import com.yunho.king.databinding.ActivityMainBinding
import com.yunho.king.presentation.service.MainService
import com.yunho.king.presentation.ui.base.BaseActivity

class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        startForegroundService(Intent(this, MainService::class.java))
    }
}