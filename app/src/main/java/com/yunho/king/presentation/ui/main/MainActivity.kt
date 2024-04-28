package com.yunho.king.presentation.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.yunho.king.R
import com.yunho.king.databinding.ActivityMainBinding
import com.yunho.king.presentation.service.MainService
import com.yunho.king.presentation.ui.base.BaseActivity

class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        startForegroundService(Intent(this, MainService::class.java))
    }
}