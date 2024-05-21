package com.yunho.king.presentation.ui.intro

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.yunho.king.BuildConfig
import com.yunho.king.R
import com.yunho.king.databinding.ActivityIntroBinding
import com.yunho.king.presentation.ui.base.BaseActivity
import com.yunho.king.presentation.ui.main.MainActivity

class IntroActivity : BaseActivity() {

    lateinit var binding: ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_intro)
        binding.lifecycleOwner = this

        setSplashAnimation()
    }

    fun setSplashAnimation() = with(binding) {
        val anim = AnimationUtils.loadAnimation(this@IntroActivity, R.anim.anim_appear)

        binding.splash.startAnimation(anim)
    }
}