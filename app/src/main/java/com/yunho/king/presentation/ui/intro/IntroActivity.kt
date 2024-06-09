package com.yunho.king.presentation.ui.intro

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import com.yunho.king.R
import com.yunho.king.Utils.PermManager
import com.yunho.king.databinding.ActivityIntroBinding
import com.yunho.king.presentation.ui.base.BaseActivity
import com.yunho.king.presentation.ui.main.MainActivity
import com.yunho.king.presentation.ui.perm.PermActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroActivity : BaseActivity() {

    lateinit var binding: ActivityIntroBinding
    private val viewModel: IntroViewModel by viewModels()
    val permManager = PermManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater, null, false)

        setSplashAnimation()
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        startIntro()
    }

    private fun setSplashAnimation() = with(binding) {
        val anim = AnimationUtils.loadAnimation(this@IntroActivity, R.anim.anim_appear)

        splash.startAnimation(anim)
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