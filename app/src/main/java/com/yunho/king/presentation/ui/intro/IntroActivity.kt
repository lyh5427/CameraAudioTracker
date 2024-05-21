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
import com.yunho.king.Utils.PermManager
import com.yunho.king.databinding.ActivityIntroBinding
import com.yunho.king.presentation.ui.base.BaseActivity
import com.yunho.king.presentation.ui.main.MainActivity
import com.yunho.king.presentation.ui.perm.PermActivity

class IntroActivity : BaseActivity() {

    lateinit var binding: ActivityIntroBinding
    val permManager = PermManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_intro)
        binding.lifecycleOwner = this

        setSplashAnimation()
    }

    override fun onResume() {
        super.onResume()
        startIntro()
    }

    fun setSplashAnimation() = with(binding) {
        val anim = AnimationUtils.loadAnimation(this@IntroActivity, R.anim.anim_appear)

        binding.splash.startAnimation(anim)
    }

    private fun startIntro() {
        if (permManager.isAllPermAllow()) {
            insertCameraPackage()
            insertAudioPackage()
            navigateToMain()
        } else {
            navigateToPerm()
        }
    }

    private fun navigateToPerm() {
        startActivity(
            Intent(this, PermActivity::class.java)
        )
    }

    private fun navigateToMain() {
        startActivity(
            Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
        )
    }
}