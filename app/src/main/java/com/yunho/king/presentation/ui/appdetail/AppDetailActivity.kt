package com.yunho.king.presentation.ui.appdetail

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.yunho.king.Const
import com.yunho.king.R
import com.yunho.king.databinding.ActivityAppDetailBinding
import com.yunho.king.presentation.ui.base.BaseActivity

class AppDetailActivity : BaseActivity() {

    lateinit var binding: ActivityAppDetailBinding
    val viewModel: AppDetailViewModel by viewModels()

    lateinit var pkgName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAppDetailBinding.inflate(layoutInflater)

        pkgName = intent.getStringExtra((Const.PKG_NAME))?: ""
    }

    fun setObserver() = with(binding) {

    }

    fun setView() = with(binding) {

    }


}