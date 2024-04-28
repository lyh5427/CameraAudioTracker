package com.yunho.king.presentation.ui.base

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yunho.king.R

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun moveToIntent(intent: Intent) {
        startActivity(intent)
    }

}