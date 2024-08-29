package com.yunho.king.presentation.ui.cameraintercept

import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.hardware.camera2.CameraManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.common.util.concurrent.ListenableFuture
import com.yunho.king.Const
import com.yunho.king.GlobalApplication
import com.yunho.king.Status.TEXT
import com.yunho.king.Utils.singleClickListener
import com.yunho.king.databinding.ActivityCameraInterceptBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class CameraInterceptActivity : AppCompatActivity() {

    companion object {
        // onCameraUnavailable이 여러번 호출되어 Activity 중복 방지를 위한 최후의 수단
        var isRunning = false
    }

    lateinit var binding: ActivityCameraInterceptBinding
    val viewModel: CameraInterceptViewModel by viewModels()


    lateinit var cameraManager: CameraManager
    lateinit var cameraIds: Array<String>
    lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    lateinit var cameraProvider: ProcessCameraProvider
    lateinit var appName: String

    var isCameraAlimCheckBox = false
    var isAppAlimCheckBox = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraInterceptBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        viewModel.packageName = intent.getStringExtra(Const.PKG_NAME)?: ""
        viewModel.getCameraAppData()
        lifecycleScope.launch { setObserver() }
        setAdmobview()
    }

    override fun onResume() {
        super.onResume()
        viewModel.setAppInfo(packageManager)
        cameraIds = cameraManager.cameraIdList
        setListener()
        openCamera2()
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
    }

    private fun setAdmobview() = with(binding) {
        admobView.adListener = object : AdListener() {
            override fun onAdFailedToLoad(error: LoadAdError) {
                Log.e(GlobalApplication.TagName, error.message)
            }
        }
        val adRequest = AdRequest.Builder().build()
        admobView.loadAd(adRequest)
    }

    private fun setListener() = with(binding) {
        cameraAlimCheckBox.singleClickListener {
            cameraAlimCheckBox.isChecked = !isCameraAlimCheckBox
        }

        appAlimCheckBox.singleClickListener {
            appAlimCheckBox.isChecked = !isAppAlimCheckBox
        }

        cancel.singleClickListener {
            isRunning = false
            setAppAlim()
            closeCamera()
        }

        btnPopupOk.singleClickListener {
            startActivity(
                Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:${viewModel.packageName}"))
            )
            closeCamera()
        }
    }

    private suspend fun setObserver() = with(binding){
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.appIcon.collectLatest {
                appImg.setImageDrawable(it)
            }
        }
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.appName.collectLatest {
                when (it.status) {
                    TEXT -> appName.text = it.toString()
                }
            }
        }
    }

    private fun openCamera2() {
        try {
            cameraProviderFuture.addListener(
                {
                    cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder()
                        .build()

                    preview.setSurfaceProvider(binding.cameraPreview.surfaceProvider)

                    val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(this, cameraSelector, preview)
                    } catch (e: Exception) {

                    }
                },
                ContextCompat.getMainExecutor(this))
        } catch (e: Exception) {
            Log.d(GlobalApplication.TagName, "aaa ${e.message}")
        }
    }

    private fun closeCamera() {
        try {
            cameraProvider.unbindAll()
        } catch (e: Exception) {
            Log.d(GlobalApplication.TagName, "${e.message}")
        }
        finish()
    }

    private fun setAppAlim() = with(binding) {
        if (appAlimCheckBox.isChecked) {
            GlobalApplication.prefs!!.appAlim = false
        }

        if (cameraAlimCheckBox.isChecked) {
            viewModel.updateNotiFlag()
        }
    }
}