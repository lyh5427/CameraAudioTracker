package com.yunho.king.presentation.ui.intro

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
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

    private val permManager = PermManager(this)
    private val updateLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()) { result: ActivityResult ->
        if (result.resultCode != RESULT_OK) {
            navigateToMain()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)

        setSplashAnimation()

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
            checkEnableUpdate()
        } else {
            navigateToPerm()
        }
    }

    private fun checkEnableUpdate() {
        val appUpdateManager = AppUpdateManagerFactory.create(this)

        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    updateLauncher,
                    AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build())
            } else {
                navigateToMain()
            }
        }
    }

    private fun navigateToPerm() {
        startActivity(
            Intent(this, PermActivity::class.java)
        )
    }

    private fun navigateToMain() {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(
                Intent(this, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                }
            )
        }, 3000)
    }
}